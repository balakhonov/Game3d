<%--@elvariable id="TANK_TYPE" type="java.lang.Integer"--%>
<%--@elvariable id="USER" type="game3d.mapping.User"--%>
<%--@elvariable id="TANK_OBJ_SET" type="java.util.Set"--%>
<%--@elvariable id="SESSION_ID" type="java.lang.String"--%>
<%--@elvariable id="ROOM_ID" type="java.lang.Integer"--%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Room ${ROOM_ID}</title>
    <script type="text/javascript">
        var ROOM_ID = "${ROOM_ID}";
        var SESSION_ID = "${SESSION_ID}";
        var TANK_OBJ_SET = ${TANK_OBJ_SET};
        var USER = ${USER};
        var TANK_TYPE = "${TANK_TYPE}";
    </script>

    <!-- JQuery -->
    <script type='text/javascript' src="/resources/media/js/jquery/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="/resources/media/js/jquery/jquery.websocket-0.0.1.js"></script>
    <script type="text/javascript" src="/resources/media/js/jquery/jquery.json-2.3.min.js"></script>


    <!-- ThreeJS -->
    <script type='text/javascript' src="/resources/media/js/three.js"></script>
    <script type='text/javascript' src="/resources/media/js/BinaryLoader.js"></script>
    <script type='text/javascript' src="/resources/media/js/ObjectLoader.js"></script>
    <script type='text/javascript' src="/resources/media/js/OBJLoader2.js"></script>
    <script type='text/javascript' src="/resources/media/js/MTLLoader.js"></script>
    <script type='text/javascript' src="/resources/media/js/OBJMTLLoader.js"></script>
    <script type='text/javascript' src="/resources/media/js/AssimpJSONLoader.js"></script>

    <!-- Libs -->
    <script type='text/javascript' src="/resources/media/js/query-loader.js"></script>
    <script type='text/javascript' src="/resources/media/js/truxtrax.util.atmosphere.js"></script>
    <script type='text/javascript' src="/resources/media/js/util.js"></script>
    <script type='text/javascript' src="/resources/media/js/three-extensions.js"></script>
    <script type='text/javascript' src="/resources/media/js/key-listener.js"></script>
    
    <style type="text/css">
    	.QOverlay {
			background-color: #ffffff;
			z-index: 9999999;
		}
	    .QLoader {
			background-color: #cccccc;
			height: 1px;
		}
		.QOverlay .logo {
			position: absolute;
			left: -180px;
			top: 50%;
			margin-top: -20px;
			margin-left: -90px;
			width: 180px;
			height: 32px;
			z-index: 1;
			text-decoration: none;
			color:#000;
		}
		.QOverlay label {
			position: absolute;
			top: 22px;
			left: 5px;
			color: #000000;
		}
		
		ul {
			margin: 0;
			padding:0;
			list-style: none;
		}
		
		ul.top-bars li {
			border:solid 1px #000000;
			width: 100px;
			text-align: center;
			font-size: 12px;
			font-weight: bold;
		}
		
		.health-bar {
			
		}
		
		.exp-bar {
			
		}
		
		.gradient-green {
			background: #b4ddb4; /* Old browsers */
			background: -moz-linear-gradient(top, #b4ddb4 0%, #83c783 17%, #52b152 33%, #008a00 67%, #005700 83%, #002400 100%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,#b4ddb4), color-stop(17%,#83c783), color-stop(33%,#52b152), color-stop(67%,#008a00), color-stop(83%,#005700), color-stop(100%,#002400)); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top, #b4ddb4 0%,#83c783 17%,#52b152 33%,#008a00 67%,#005700 83%,#002400 100%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top, #b4ddb4 0%,#83c783 17%,#52b152 33%,#008a00 67%,#005700 83%,#002400 100%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top, #b4ddb4 0%,#83c783 17%,#52b152 33%,#008a00 67%,#005700 83%,#002400 100%); /* IE10+ */
			background: linear-gradient(to bottom, #b4ddb4 0%,#83c783 17%,#52b152 33%,#008a00 67%,#005700 83%,#002400 100%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#b4ddb4', endColorstr='#002400',GradientType=0 ); /* IE6-9 */
		}
		
		.gradient-gray {
			background: rgb(245,246,246); /* Old browsers */
			background: -moz-linear-gradient(top, rgba(245,246,246,1) 0%, rgba(219,220,226,1) 21%, rgba(184,186,198,1) 49%, rgba(199,201,204,1) 80%, rgba(191,191,191,1) 100%); /* FF3.6+ */
			background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(245,246,246,1)), color-stop(21%,rgba(219,220,226,1)), color-stop(49%,rgba(184,186,198,1)), color-stop(80%,rgba(199,201,204,1)), color-stop(100%,rgba(191,191,191,1))); /* Chrome,Safari4+ */
			background: -webkit-linear-gradient(top, rgba(245,246,246,1) 0%,rgba(219,220,226,1) 21%,rgba(184,186,198,1) 49%,rgba(199,201,204,1) 80%,rgba(191,191,191,1) 100%); /* Chrome10+,Safari5.1+ */
			background: -o-linear-gradient(top, rgba(245,246,246,1) 0%,rgba(219,220,226,1) 21%,rgba(184,186,198,1) 49%,rgba(199,201,204,1) 80%,rgba(191,191,191,1) 100%); /* Opera 11.10+ */
			background: -ms-linear-gradient(top, rgba(245,246,246,1) 0%,rgba(219,220,226,1) 21%,rgba(184,186,198,1) 49%,rgba(199,201,204,1) 80%,rgba(191,191,191,1) 100%); /* IE10+ */
			background: linear-gradient(to bottom, rgba(245,246,246,1) 0%,rgba(219,220,226,1) 21%,rgba(184,186,198,1) 49%,rgba(199,201,204,1) 80%,rgba(191,191,191,1) 100%); /* W3C */
			filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#f5f6f6', endColorstr='#bfbfbf',GradientType=0 ); /* IE6-9 */
		}
	</style>
</head>
<body style="margin: 0; padding: 0;">
<div style="position:absolute; left:10px; top:10px;">
	<ul class="top-bars">
		<li><div class="health-bar gradient-green">100/100</div></li>
		<li><div class="exp-bar gradient-gray">100/100</div></li>
	</ul>
</div>
<script type='text/javascript' src="/resources/media/js/main.js"></script>
</body>
</html>