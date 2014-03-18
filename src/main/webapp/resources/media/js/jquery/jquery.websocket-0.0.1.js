(function($) {
	$.extend({
		websocketSettings : {
			open : function() {
				console.info('Connected!');
			},
			close : function() {
				console.warn('Closed!');
			},
			error : function() {
				console.error('Error!');
			},
			notAvailable : function() {
				console.warn('Server not available!');
			},
			available : function() {
				console.warn('Server available!');
			},
			message : function(e) {
				if (isEmpty(e.originalEvent.data)) {
					return;
				}

				var m = $.evalJSON(e.originalEvent.data);
				var h = $.websocketSettings.events[m.type];
				if (isFunction(h)) {
					h();
				} else {
					for ( var index in h) {
						var callbackFunction = h[index];
						callbackFunction(m.data);
					}
				}
			},
			options : {},
			events : {}
		},
		websocket : function(url, s) {
			if (!WebSocket) {
				console.warn('WebSockets not support!');
				return;
			}

			var serverAvailable = true;
			var pingTimer;
			var responseTimeoutTimer;
			var responseTimeout = 3000;
			var pingServer = true;

			try {
				var ws = new WebSocket(url);
			} catch (e) {
				console.error(e);
				return;
			}

			ws._settings = $.extend($.websocketSettings, s);

			$(ws).bind('open', function(e) {
				pingServer = true;
				$.websocketSettings.open(e);
			});
			$(ws).bind('close', function(e) {
				pingServer = false;
				clearTimeout(pingTimer);
				$.websocketSettings.close(e);
			});
			$(ws).bind('reconnect', function(e) {
				$.websocketSettings.reconnect(e);
			});
			$(ws).bind('error', $.websocketSettings.error);
			$(ws).bind('message', function(e) {
				clearTimeout(responseTimeoutTimer);

				if (!serverAvailable) {
					serverAvailable = true;
					$.websocketSettings.available();
				}

				$.websocketSettings.message(e);
			});

			ws._send = ws.send;
			ws.send = function(type, data) {
//				responseTimeoutTimer = setTimeout(function() {
//					clearTimeout(responseTimeoutTimer);
//					serverAvailable = false;
//
//					ws.close();
//
//					$.websocketSettings.notAvailable();
//					$.websocketSettings.reconnect();
//				}, responseTimeout);

				if (type == "ping") {
					return this._send("\r");
				}

				var m = {
					type : type
				};
				m = $.extend(true, m, $.extend(true, {}, $.websocketSettings.options, m));
				if (data)
					m['data'] = data;
				return this._send($.toJSON(m));
			};

			$(window).unload(function() {
				ws.close();
				ws = null;
			});

			//initPing(ws);

			function initPing(ws) {
				pingTimer = setTimeout(function() {
					initPing(ws);
				}, responseTimeout);

				if (ws.readyState == 1) {
					if (pingServer) {
						ws.send("ping");
					}
				}
			}
			return ws;
		}
	});
})(jQuery);