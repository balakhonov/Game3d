var QueryLoader = {
		overlay : "",
		loadBar : "",
		preloader : "",
		items : new Array(),
		doneStatus : 0,
		doneNow : 0,
		selectorPreload : "body",
		ieLoadFixTime : 2000,
		ieTimeout : "",

		additionDownloadsCount : 0,
		withLogo : false,
		logo : null,

		LOGO_HREF : "/",
		LOGO_CLASS : "logo",
		LOGO_NAME : "LOGO NAME",

		init : function(callback) {
			if (navigator.userAgent.match(/MSIE (\d+(?:\.\d+)+(?:b\d*)?)/) == "MSIE 6.0,6.0") {
				// break if IE6
				return false;
			}

			if (QueryLoader.selectorPreload == "body") {
				QueryLoader.spawnLoader();
				QueryLoader.getImages(QueryLoader.selectorPreload);
				// QueryLoader.createPreloading();
			} else {
				$(document).ready(function() {
					QueryLoader.spawnLoader();
					QueryLoader.getImages(QueryLoader.selectorPreload);
					// QueryLoader.createPreloading();
				});
			}

			// help IE drown if it is trying to die :)
			QueryLoader.ieTimeout = setTimeout("QueryLoader.ieLoadFix()", QueryLoader.ieLoadFixTime);

			QueryLoader.logo.animate({
				left : 50 + "%"
			}, 500, "linear", function() {
				QueryLoader.createPreloading();
				if (callback)
					callback();
			});
		},

		ieLoadFix : function() {
			var ie = navigator.userAgent.match(/MSIE (\d+(?:\.\d+)+(?:b\d*)?)/);
			if (ie && ie[0].match("MSIE")) {
				while ((100 / QueryLoader.doneStatus) * QueryLoader.doneNow < 100) {
					QueryLoader.imgCallback();
				}
			}
		},

		imgCallback : function() {
			QueryLoader.downloadDoneCallback();
		},

		downloadDoneCallback : function() {
			QueryLoader.doneNow++;
			QueryLoader.animateLoader();
		},

		getImages : function(selector) {
			var everything = $(selector).find("*:not(script)").each(
					function() {
						var url = "";

						if ($(this).css("background-image") != "none") {
							var url = $(this).css("background-image");
						} else if (typeof ($(this).attr("src")) != "undefined" && $(this).attr("tagName")
								&& $(this).attr("tagName").toLowerCase() == "img") {
							var url = $(this).attr("src");
						}

						if (!url.indexOf("url(") == 0) {
							return;
						}

						url = url.replace("url(\"", "");
						url = url.replace("url(", "");
						url = url.replace("\")", "");
						url = url.replace(")", "");

						if (url.length > 0) {
							QueryLoader.items.push(url);
						}
					});
		},

		createPreloading : function() {
			QueryLoader.preloader = $("<div></div>").appendTo(QueryLoader.selectorPreload);
			$(QueryLoader.preloader).css({
				height : "0px",
				width : "0px",
				overflow : "hidden"
			});

			var length = QueryLoader.items.length;
			QueryLoader.doneStatus = length;
			QueryLoader.doneStatus += QueryLoader.additionDownloadsCount;

			for ( var i = 0; i < length; i++) {
				var url = QueryLoader.items[i];
				if (url) {
					var imgLoad = $("<img/>");
					$(imgLoad).attr("src", url);
					$(imgLoad).unbind("load");
					$(imgLoad).bind("load", function() {
						QueryLoader.imgCallback();
					});
					$(imgLoad).appendTo($(QueryLoader.preloader));
				}
			}

		},

		spawnLoader : function() {
			if (QueryLoader.selectorPreload == "body") {
				var height = $(window).height();
				var width = $(window).width();
				var position = "fixed";
			} else {
				var height = $(QueryLoader.selectorPreload).outerHeight();
				var width = $(QueryLoader.selectorPreload).outerWidth();
				var position = "absolute";
			}
			var left = $(QueryLoader.selectorPreload).offset()['left'];
			var top = $(QueryLoader.selectorPreload).offset()['top'];

			QueryLoader.overlay = $("<div></div>").appendTo($(QueryLoader.selectorPreload));
			$(QueryLoader.overlay).addClass("QOverlay");
			$(QueryLoader.overlay).css({
				position : position,
				top : top,
				left : left,
				width : (width + 30) + "px",
				height : height + "px"
			});

			if (QueryLoader.withLogo) {
				QueryLoader.logo = $("<a>"+QueryLoader.LOGO_NAME+"<label>Loading.. <span class='perc'>0%</span></label></a>");
				QueryLoader.logo.attr("href", QueryLoader.LOGO_HREF);
				QueryLoader.logo.addClass(QueryLoader.LOGO_CLASS);

				QueryLoader.logo.appendTo($(QueryLoader.overlay));
			}

			QueryLoader.loadBar = $("<div></div>").appendTo($(QueryLoader.overlay));
			$(QueryLoader.loadBar).addClass("QLoader");

			$(QueryLoader.loadBar).css({
				position : "relative",
				top : "50%",
				width : "0%"
			});
		},

		animateLoader : function() {
			var perc = (100 / QueryLoader.doneStatus) * QueryLoader.doneNow;

			if (QueryLoader.doneStatus == QueryLoader.doneNow) {
				$(QueryLoader.loadBar).stop().animate({
					width : perc + "%"
				}, 100, "linear", function() {
					if (QueryLoader.withLogo) {
						$(".perc", QueryLoader.logo).html(parseInt(perc) + "%");

						$(QueryLoader.loadBar).animate({
							top : 0
						}, 500, "linear", function() {
						});

						QueryLoader.logo.animate({
							left : 100 + "%",
							opacity : 0
						}, 500, "linear", function() {
							QueryLoader.doneLoad();
						});
					}
				});
			} else {
				if (parseInt(perc) % 20 == 0) {
					$(QueryLoader.loadBar).stop();
				} else if (parseInt(perc) % 1 == 0) {
					$(QueryLoader.loadBar).animate({
						width : perc + "%"
					}, 100, "linear", function() {
						if (QueryLoader.withLogo) {
							$(".perc", QueryLoader.logo).html(parseInt(perc) + "%");
						}
					});
				}
			}
		},

		doneLoad : function() {
			// console.log("images loaded");

			// prevent IE from calling the fix
			clearTimeout(QueryLoader.ieTimeout);

			// determine the height of the preloader for the effect
			if (QueryLoader.selectorPreload == "body") {
				var height = $(window).height();
			} else {
				var height = $(QueryLoader.selectorPreload).outerHeight();
			}

			// The end animation, adjust to your likings
			$(QueryLoader.loadBar).animate({
				opacity : 0
			// top : 0
			}, 400, "linear", function() {
				$(QueryLoader.overlay).fadeOut(500);
				$(QueryLoader.preloader).remove();
			});
		}
	};