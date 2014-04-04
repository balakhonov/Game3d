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

    <link href="http://bootstrap-ru.com/assets/css/bootstrap.css" rel="stylesheet"/>
    <link href="/resources/media/css/main.css" rel="stylesheet"/>

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
    <script type='text/javascript' src="/resources/media/js/util.js"></script>
    <script type='text/javascript' src="/resources/media/js/three-extensions.js"></script>
    <script type='text/javascript' src="/resources/media/js/key-listener.js"></script>

    <script type="text/html" id="not-support-html">
        Your graphics card does not seem to support <a href="http://khronos.org/webgl/wiki/Getting_a_WebGL_Implementation" style="color:#000">WebGL</a>.
        <br/>
        Find out how to get it <a href="http://get.webgl.org/" style="color:#000">here</a>.
    </script>
</head>
<body>
<div id="webgl-not-suppoerted"><span></span></div>
<div class="lighter"></div>
<div id="header-wrapper">
    <div id="header">
        <div class="header-menu-wrapper">
            <div id="pngfix-right"></div>
            <ul class="menu">
                <li class="page_item"><a title="" href="/">Main</a></li>
                <li class="page_item"><a title="Feedback" href="#">Feedback</a></li>
            </ul>
            <div id="pngfix-left"></div>
        </div>
    </div>
</div>

<div id="content-wrapper">
    <div id="content-header">
    </div>
    <div id="container">
        <div id="main">
            <form action="/room" method="post">
                <div class="left">
                    <div id="canvas" style="width:500px;height: 300px;"></div>
                    <ul class="tank-list">
                    </ul>
                </div>
                <div class="right">
                    <select class="rooms-list" multiple="multiple" name="roomId">
                        <option value="1" selected>Room 1</option>
                    </select>
                    <input type="hidden" name="tankType" value="0"/>
                    <br/>
                </div>
                <button type="submit" class="">Go!</button>
            </form>
        </div>
    </div>
</div>
<script src="/resources/media/js/Detector.js"></script>
<script type='text/javascript' src="/resources/media/js/account.js"></script>

<div class="copyright">
    Created By <a href="http://www.linkedin.com/profile/view?id=217815692" title="Balakhonov Yurii">Balakhonov Yurii</a>
</div>
</body>
</html>