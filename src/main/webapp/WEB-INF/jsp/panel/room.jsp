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
    <script type='text/javascript' src="/resources/media/js/truxtrax.util.atmosphere.js"></script>
    <script type='text/javascript' src="/resources/media/js/util.js"></script>
    <script type='text/javascript' src="/resources/media/js/three-extensions.js"></script>
    <script type='text/javascript' src="/resources/media/js/key-listener.js"></script>
</head>
<body style="margin: 0; padding: 0;">
<script type='text/javascript' src="/resources/media/js/main.js"></script>
</body>
</html>