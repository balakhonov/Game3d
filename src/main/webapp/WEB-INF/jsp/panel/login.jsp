<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="ISO-8859-1" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Title</title>
    <link href="http://bootstrap-ru.com/assets/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
        .sign-in-form {
            position: absolute;
            width: 300px;
            left: 50%;
            margin-left: -150px;
            margin-top: 100px;
            padding: 10px;
            border: 1px solid #dddddd;
            border-collapse: separate;
            -webkit-border-radius: 4px;
            -moz-border-radius: 4px;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<div class="sign-in-form table-bordered">
    <form action="/" method="post">
        <fieldset>
            <legend>Enter to game</legend>
            <label>User name:</label>
            <input type="text" placeholder="Type your name here" name="user-name"/>
            <br/>
            <button type="submit" class="btn">Sign in</button>
        </fieldset>
    </form>
</div>
</body>
</html>