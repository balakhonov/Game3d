<%--@elvariable id="VALIDATION_ERROR" type="String"--%>
<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Title</title>
    <link href="http://bootstrap-ru.com/assets/css/bootstrap.css" rel="stylesheet"/>
    <link href="/resources/media/css/main.css" rel="stylesheet"/>

    <script type='text/javascript' src="/resources/media/js/jquery/jquery-1.8.3.js"></script>

    <script type="text/javascript">
        $(function() {
            $("#start-button").on("click", postAuth);

            function postAuth() {
                console.log("postAuth");

                $(".error-label").hide();

                var data = {};
                data.userName = $("input[name=user-name]").val();

                $.post("/", data, callbackPostAuth);
            }

            /**
             *
             * @param {{RESULT_CODE:number, MESSAGE:string}} data
             */
            function callbackPostAuth(data) {
                console.log("callbackPostAuth", data);
                switch (data.RESULT_CODE) {
                    case 0:
                        window.location = "/";
                        break;
                    case 1:
                        $(".error-label").html(data.MESSAGE);
                        $(".error-label").show();
                        break;
                    default:
                        console.error("Unknown error: " + data.RESULT_CODE + ", " + data.MESSAGE);
                }
            }
        });
    </script>
</head>
<body>
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
<div class="sign-in-form table-bordered">
    <fieldset>
        <legend>Start the game</legend>
        <label>User name:</label>
        <input type="text" placeholder="Type your name here" name="user-name"/>

        <div class="error-label"></div>
        <button id="start-button">Start</button>
    </fieldset>
</div>

<div class="copyright">
    Created By <a href="http://www.linkedin.com/profile/view?id=217815692" title="Balakhonov Yurii">Balakhonov Yurii</a>
</div>
</body>
</html>