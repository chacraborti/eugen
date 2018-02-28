<html>
<head>
    <title>Just page</title>
</head>
<body>
<h1>Hi there!</h1>

<div>GET form</div>
<br/>
<form>
    <div>First Name:</div>
    <input type="text" name="first_name"/>
    <br/>
    <div>Last Name:</div>
    <input type="text" name="last_name"/>
    <input type="submit" value="Submit GET"/>
</form>

<div>POST form</div>
<br/>
<form action="/" method="post">
    <div>First Name:</div>
    <input type="text" name="first_name"/>
    <br/>
    <div>Last Name:</div>
    <input type="text" name="last_name"/>
    <input type="submit" value="Submit POST"/>
</form>
</body>
</html>
