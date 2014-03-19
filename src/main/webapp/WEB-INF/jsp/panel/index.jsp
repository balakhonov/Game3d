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

    <!-- JQuery -->
    <script type='text/javascript' src="/resources/media/js/jquery/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="/resources/media/js/jquery/jquery.websocket-0.0.1.js"></script>
    <script type="text/javascript" src="/resources/media/js/jquery/jquery.json-2.3.min.js"></script>

    <script type="text/javascript">
        var ROOM_ID = "${ROOM_ID}";
        var SESSION_ID = "${SESSION_ID}";
        var TANK_OBJ_SET = ${TANK_OBJ_SET};
        var USER = ${USER};
    </script>

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

    <style type="text/css">
        #container {
            width: 800px;
            position: absolute;
            left: 50%;
            margin-left: -400px;
        }

        .left {
            float: left;
        }

        .right {
            float: right;
        }

        .tank-list {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        .tank-list li {
            float: left;
            width: 100px;
            height: 60px;
            margin: 2px;
            border: 1px solid #dddddd;
            border-collapse: separate;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
            cursor: pointer;
        }

        .tank-list li:hover {
            background: #eeeeee;
        }
    </style>
</head>
<body style="margin: 0; padding: 0;">
<div id="container">
    <div class="left">
        <div id="canvas" style="width:500px;height: 300px;"></div>
        <ul class="tank-list">
        </ul>
    </div>
    <div class="right">
        <form action="/room" method="post">
            <select class="rooms-list" multiple="multiple" name="roomId">
                <option value="1" selected>Room 1</option>
                <option value="2">Room 2</option>
            </select>
            <input type="hidden" name="tankType" value="0"/>
            <br/>
            <button type="submit" class="btn">Start</button>
        </form>
    </div>
</div>
<script type='text/javascript' src="/resources/media/js/account.js"></script>
</body>
</html>