$(function () {
	if (!Detector.webgl) {
		var popup = $("#webgl-not-suppoerted");
		popup.find("span").html($("#not-support-html").html());
		popup.show();
		return;
	}

	var errorLabel = $(".error-label");
	var startButton = $("#start-button");

	init();

	function init() {
		startButton.on("click", postAuth);
	}

	function postAuth() {

		errorLabel.hide();

		var data = {};
		data.userName = $("input[name=user-name]").val();

		$.post("/", data, callbackPostAuth);
	}

	/**
	 *
	 * @param {{RESULT_CODE:number, MESSAGE:string}} data
	 */
	function callbackPostAuth(data) {
		switch (data.RESULT_CODE) {
			case 0:
				window.location = "/";
				break;
			case 1:
				errorLabel.html(data.MESSAGE);
				errorLabel.show();
				break;
			default:
				console.error("Unknown error: " + data.RESULT_CODE + ", " + data.MESSAGE);
		}
	}
});