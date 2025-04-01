<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test WAF</title>
</head>
<body>
    <h2>Try Entering Malicious Input</h2>
    <form method="GET" action="">
        <input type="text" name="user_input" placeholder="Type SQL, XSS, or CSRF payloads" required>
        <button type="submit">Submit</button>
    </form>
</body>
</html>
