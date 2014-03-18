var SOCKET_CONTROLLER = new SocketController();

var events = {
	unauthorized : function() {
		if (errorOverlay) {
			errorOverlay.showSessionClosed();
		} else {
			document.location.reload();
		}
	}
};

function SocketController() {
	var that = this;
	var ws = {};
	var disconnects = 0;
	var delay = 1;// Seconds
	var reconnecting = false;
	var reconnecTimer = null;

	if (!SESSION_ID) {
		throw new Error("Property SESSION_ID not initialized");
	}

	this.stop = function() {
		ws.stop();
	};

	this.send = function(type, message) {
		if (ws.send) {
			ws.send(type, message);
		}
	}

	function wsClose() {
		setTimeout(function() {
			console.info('WebSocket closing..');

			if (ws) {
				ws.close();
			}
		}, 1000);
	}

	this.wsConnect = function() {
		console.info('WebSocket connecting..');
		clearTimeout(reconnecTimer);
		reconnecting = false;

		ws = $.websocket("ws://{0}:9879/{1}".format(document.location.hostname,
				SESSION_ID), {
			open : function() {
				console.info('Connected!', ws);
				disconnects = 0;

				serverAvailable();
			},
			close : function(e, a) {
				console.warn('Closed', e, a);
				if (ws.readyState == ws.CLOSED || ws.readyState == ws.CLOSING) {
					wsReConnect();
				}
			},
			reconnect : function(e, a) {
				console.warn('Reconnect', e, a);
				wsReConnect();
			},
			error : function(e, a) {
				console.error('Error!', e, a);
				wsReConnect();
			},
			notAvailable : function() {
				serverNotAvailable();
			},
			available : function() {
				serverAvailable();
			},
			events : events
		});
	};

	function serverAvailable() {
		console.info("server is available");
	}

	function serverNotAvailable() {
		console.info("server is not available");
	}

	function waitServerNotAvailable() {
		console.info("wait.. server is not available");
	}

	function wsReConnect() {
		if (reconnecting) {
			return;
		}
		reconnecting = true;

		disconnects++;
		delay = 3 + disconnects * 3;
		if (delay > 60) {
			delay = 60;
		}

		wait();
		function wait() {
			reconnecTimer = setTimeout(function() {
				delay = parseInt(delay - 1);
				if (delay <= 0) {
					waitServerNotAvailable();
					that.wsConnect();
				} else {
					waitServerNotAvailable();
					that.wsConnect();
				}
			}, 1000);
		}
	}

	this.forceConnect = function() {
		console.info("force connect", ws.readyState);
		serverNotAvailable();
		SOCKET_CONTROLLER.wsConnect();
	};
}

function AtmosphereModel() {
	var listeners = [];

	this.addListener = function(name, callback) {
		if (!isString(name)) {
			throw new Error("name(" + name + ") should be a string");
		}

		if (!isFunction(callback)) {
			throw new Error("callback(" + callback + ") should be a function");
		}

		if (!isArray(listeners[name])) {
			listeners[name] = [];
		}

		listeners[name].push(callback);

		if (!isArray(events[name])) {
			events[name] = [];
		}
		events[name].push(callback);
	};

	/**
	 * 
	 * @param {{act:string,
	 *            data:object }} receivedData
	 */
	function receiveAtmosphereData(receivedData) {
		if (receivedData == null) {
			return;
		}

		var act = receivedData.act;
		var data = receivedData.data;

		if (!act) {
			throw new Error("Unknown receivedData format");
		}

		if (!data) {
			throw new Error("Unknown receivedData format");
		}

		if (!listeners[act]) {
			throw new Error("listener(" + act + ") not found");
		}

		for ( var index in listeners[act]) {
			var callbackFunction = listeners[act][index];
			callbackFunction(data);
		}
	}
}

function AtmosphereHandle(url, callback) {
	if (typeof (url) != "string") {
		throw Error("Url should be as 'string' type!");
	}
	if (typeof (callback) != "function") {
		throw Error("Callback should be as 'function' type!");
	}

	var socket = $.atmosphere;
	var connected = false;
	var subSocket;
	var reconnectDelay = 5000;
	var reconnectErrorsCount = 0;

	$(function() {

	});

	var asyncHttpStatistics = {
		transportType : 'N/A',
		responseState : 'N/A',
		numberOfCallbackInvocations : 0,
		numberOfMessages : 0,
		numberOfErrors : 0
	};

	var request = new $.atmosphere.AtmosphereRequest();
	request.transport = "streaming";// "streaming", websocket,long-polling;
	request.url = ROOT_PATH + url;
	request.logLevel = 'info';
	request.contentType = "application/json";
	request.fallbackTransport = "ajax";
	request.fallbackMethod = "POST";
	request.maxStreamingLength = 100000000;

	String.prototype.endsWith = function(suffix) {
		return this.indexOf(suffix, this.length - suffix.length) !== -1;
	};

	var streamMessage = "";

	request.onMessage = function(response) {
		// subSocket.pushLocal(response);
		// showLog(response);
		asyncHttpStatistics.numberOfCallbackInvocations++;

		// console.log("response", response);
		if (response.state = "messageReceived" && response.status == 200) {
			var message = response.responseBody;
			// console.log("message", message);

			if ("close socket" == message) {
				subSocket.close();
				connected = false;
				errorOverlay.showMultipleTabsError();
				$.atmosphere.log('warn', [ "SOCkET WAS CLOSE" ]);
				return;
			}

			if (!message.endsWith("<!*!>")) {
				streamMessage += message;
				return;
			} else {
				message = streamMessage + message;
				streamMessage = "";
			}

			try {
				var jsonData = [];
				var dataArr = message.split('<!*!>');

				for ( var i in dataArr) {
					var obj = dataArr[i];

					if (obj.length > 0) {
						jsonData.push($.parseJSON(obj));
					}
				}

			} catch (e) {
				$.atmosphere.log('error', [
						'This does not look like a valid JSON: ', message ]);
				asyncHttpStatistics.numberOfErrors++;
				streamMessage = "";
				return;
			}

			for ( var j in jsonData) {
				callback(jsonData[j]);
			}
		} else {
			$.atmosphere.log('error', [ 'response.state: ', response.state ]);
			$.atmosphere.log('error', [ 'response.status: ', response.status ]);
		}

	};

	request.onLocalMessage = function(message) {
		$.atmosphere.log('info', [ 'onLocalMessage' ], message);
	};

	request.onMessagePublished = function(response) {
		$.atmosphere.log('info', [ 'Message Published', response ]);
	};

	request.onOpen = function(rq, rs) {
		$.atmosphere.log('info', [ 'socket open', rq, rs ]);

	};

	request.onReopen = function() {
		$.atmosphere.log('info', [ 'socket reopen' ]);
		$.atmosphere.log('warn', [ 'socket reopen' ]);
	};

	request.onError = function(response) {
		$.atmosphere.log('error', [ 'socket error', response ]);

		// tryReconnect();
		reconnectErrorsCount++;
	};

	request.onReconnect = function(request, response) {
		reconnectErrorsCount = 0;
		$.atmosphere.log('info', [ 'socket reconnect', request, response ]);
	};

	// openConnection(request);

	var isReconnecting = false;

	function tryReconnect() {
		if (isReconnecting) {
			return;
		}

		isReconnecting = true;
		$.atmosphere.log('info', [ 'Try Reconnect...' ]);

		setTimeout(function() {
			subSocket.close();
			connected = false;
			openConnection(request);
			isReconnecting = false;
		}, reconnectDelay);
	}

	function openConnection(request) {
		subSocket = socket.subscribe(request);
		connected = true;
	}

	function showLog(response) {
		$.atmosphere.log('info', [ "response: ", response ]);
		$.atmosphere.log('info', [ "response.status: " + response.status ]);
		$.atmosphere.log('info', [ "response.state: " + response.state ]);
		$.atmosphere.log('info',
				[ "response.transport: " + response.transport ]);

		$.atmosphere.log('info', [ "number Of Callback Invocations: "
				+ asyncHttpStatistics.numberOfCallbackInvocations ]);
		$.atmosphere.log('info', [ "number Of Messages: "
				+ asyncHttpStatistics.numberOfMessages ]);
		$.atmosphere.log('info', [ "number Of Errors: "
				+ asyncHttpStatistics.numberOfErrors ]);
	}

}