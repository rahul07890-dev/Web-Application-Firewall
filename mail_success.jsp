<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Email Sent Successfully</title>
    <style>
        body {
            font-family: 'Segoe UI', sans-serif;
            margin: 0;
            padding: 0;
            background-color: #e0f7fa; /* Light blue background */
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            color: #0277bd; /* Dark blue text color */
        }

        .card {
            background: white;
            padding: 40px 50px;
            border-radius: 16px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
            text-align: center;
            max-width: 500px;
            width: 90%;
        }

        .card h1 {
            font-size: 28px;
            margin-bottom: 16px;
            color: #0277bd; /* Dark blue color for heading */
        }

        .card p {
            font-size: 16px;
            color: #0288d1; /* Medium blue color for text */
            margin-bottom: 30px;
        }

        .back-link {
            display: inline-block;
            text-decoration: none;
            background-color: #039be5; /* Light blue background for button */
            color: white;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 500;
            transition: background-color 0.2s ease;
        }

        .back-link:hover {
            background-color: #0288d1; /* Darker blue on hover */
        }

        .icon {
            font-size: 48px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="card">
        <h1>Email Sent Successfully</h1>
        <p>Your email has been sent successfully. The recipient will get the notification shortly.</p>
        <a class="back-link" href="requestLogs.jsp">Back to Dashboard</a>
    </div>
</body>
</html>
