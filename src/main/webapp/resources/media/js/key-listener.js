function KeyHandler() {
	/**
	 *
	 */
	var eventStatusMap = [];

	/**
	 *
	 */
	var eventUpActionMap = [];

	/**
	 *
	 */
	var eventDownActionMap = [];

	window.addEventListener('keydown', function (e) {
		if (!eventStatusMap[e.keyCode]) {
//			console.info("keydown", e.keyCode);
			eventStatusMap[e.keyCode] = true;
			startAction(e.keyCode, eventDownActionMap);
		}
	}, false);

	window.addEventListener('keyup', function (e) {
//		console.info("keyup", e.keyCode);

		eventStatusMap[e.keyCode] = false;
		startAction(e.keyCode, eventUpActionMap);
	}, false);

	/**
	 *
	 * @param {number|Array,<number>} keyCode
	 * @param timeout
	 * @param callback
	 */
	this.addKeyDownListener = function (keyCode, timeout, callback) {
		if (!(isInteger(keyCode) || isArray(keyCode))) {
			throw new Error("keyCode({0}) should be an integer or array of integer".format(keyCode));
		}
		if (!isInteger(timeout)) {
			throw new Error("timeout({0}) should be an integer".format(timeout));
		}
		if (!isFunction(callback)) {
			throw new Error("callback({0}) should be an integer"
				.format(callback));
		}

		if (isArray(keyCode)) {
			keyCode.forEach(function (key) {
				bindCodeKey(key);
			});
		} else {
			bindCodeKey(keyCode);
		}

		function bindCodeKey(key) {
			if (!isArray(eventDownActionMap[key])) {
				eventDownActionMap[key] = [];
			}

			eventDownActionMap[key].push({
				timeout: timeout,
				callback: callback
			});
		}
	};

	this.addKeyUpListener = function (keyCode, timeout, callback) {
		if (!(isInteger(keyCode) || isArray(keyCode))) {
			throw new Error("keyCode({0}) should be an integer".format(keyCode));
		}
		if (!isInteger(timeout)) {
			throw new Error("timeout({0}) should be an integer".format(timeout));
		}
		if (!isFunction(callback)) {
			throw new Error("callback({0}) should be an integer"
				.format(callback));
		}

		if (isArray(keyCode)) {
			keyCode.forEach(function (key) {
				bindCodeKey(key);
			});
		} else {
			bindCodeKey(keyCode);
		}

		function bindCodeKey(key) {
			if (!isArray(eventUpActionMap[key])) {
				eventUpActionMap[key] = [];
			}

			eventUpActionMap[key].push({
				timeout: timeout,
				callback: callback
			});
		}
	};

	function startAction(keyCode, eventActionMap) {
		if (isArray(eventActionMap[keyCode])) {
			for (var i in eventActionMap[keyCode]) {
				var actionArray = eventActionMap[keyCode];
				var action = actionArray[i];
				action.callback();

				if (action.timeout != 0) {
					runAction(keyCode, action.timeout, action.callback);
				}
			}
		}
	}

	function runAction(keyCode, timeout, callback) {
		setTimeout(function () {
			if (eventStatusMap[keyCode]) {
				callback();
				runAction(keyCode, timeout, callback);
			}
		}, timeout);
	}
}