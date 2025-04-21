<%@ page import="java.sql.*, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Firewall Request Logs</title>
   <style>
    body {
        font-family: 'Segoe UI', sans-serif;
        background-color: #f9fafb;
        margin: 0;
        padding: 0;
    }

    header {
        background-color: #111827;
        padding: 16px 24px;
        color: white;
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        gap: 8px;
        box-shadow: 0 2px 6px rgba(0,0,0,0.1);
    }

    @media(min-width: 600px) {
        header {
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
        }
    }

    header h1 {
        font-size: 22px;
        margin: 0;
    }

    .container {
        padding: 20px 24px;
        overflow-x: auto;
    }

    h2 {
        color: #1f2937;
        font-size: 20px;
        margin-bottom: 16px;
    }

    table {
        width: 100%;
        min-width: 800px;
        border-collapse: collapse;
        margin-top: 10px;
        background-color: white;
        border-radius: 10px;
        overflow: hidden;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
    }

    th, td {
        padding: 12px 14px;
        text-align: center;
        font-size: 14px;
    }

    thead {
        background-color: #e5e7eb;
        color: #374151;
        font-weight: 600;
        position: sticky;
        top: 0;
        z-index: 10;
    }

    tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    tr.blocked {
        background-color: #fff1f2;
    }

    tr.safe {
        background-color: #f0fdf4;
    }

    .status-tag {
        font-weight: 600;
        padding: 6px 10px;
        border-radius: 20px;
        font-size: 13px;
        display: inline-block;
    }

    .status-safe {
        color: #065f46;
        background-color: #d1fae5;
    }

    .status-blocked {
        color: #991b1b;
        background-color: #fee2e2;
    }

    .action-form button {
        border: none;
        padding: 8px 12px;
        border-radius: 6px;
        cursor: pointer;
        font-weight: 500;
        font-size: 13px;
        transition: background-color 0.2s ease;
        color: white;
        white-space: nowrap;
    }

    .btn-safe {
        background-color: #2563eb;
    }

    .btn-safe:hover {
        background-color: #1d4ed8;
    }

    .btn-blocked {
        background-color: #dc2626;
    }

    .btn-blocked:hover {
        background-color: #b91c1c;
    }

    .no-logs {
        text-align: center;
        padding: 20px;
        font-style: italic;
        color: #6b7280;
    }

    footer {
        margin-top: 30px;
        text-align: center;
        color: #9ca3af;
        font-size: 13px;
        padding: 16px;
    }

    @media (max-width: 768px) {
        header h1 {
            font-size: 20px;
        }

        .container {
            padding: 16px;
        }

        th, td {
            padding: 10px;
            font-size: 13px;
        }

        .status-tag {
            font-size: 12px;
            padding: 4px 8px;
        }

        .action-form button {
            font-size: 12px;
            padding: 6px 10px;
        }
    }

    @media (max-width: 500px) {
        table {
            min-width: unset;
            font-size: 12px;
        }

        th, td {
            padding: 8px 10px;
        }

        .status-tag {
            font-size: 11px;
        }

        .action-form button {
            font-size: 11px;
        }
    }
</style>

</head>
<body>
    <header>
        <h1>🛡️ WAF Dashboard</h1>
        <span>Logged Requests Monitor</span>
    </header>

    <div class="container">
        <h2>📄 Request Activity Logs</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Method</th>
                    <th>URI</th>
                    <th>Query</th>
                    <th>IP Address</th>
                    <th>Status</th>
                    <th>Timestamp</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    String url = "jdbc:mysql://localhost:3306/firewall_db";
                    String user = "root";
                    String password = "root";

                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection(url, user, password);
                        Statement stmt = conn.createStatement();
                        ResultSet rs = stmt.executeQuery("SELECT * FROM request_logs ORDER BY timestamp DESC");

                        boolean hasLogs = false;
                        while (rs.next()) {
                            hasLogs = true;
                            boolean blocked = rs.getBoolean("blocked");
                %>
                <tr class="<%= blocked ? "blocked" : "safe" %>">
                    <td><%= rs.getInt("id") %></td>
                    <td><%= rs.getString("method") %></td>
                    <td><%= rs.getString("uri") %></td>
                    <td><%= rs.getString("query") %></td>
                    <td><%= rs.getString("ip_address") %></td>
                    <td>
                        <span class="status-tag <%= blocked ? "status-blocked" : "status-safe" %>">
                            <%= blocked ? "🚫 Blocked" : "✅ Safe" %>
                        </span>
                    </td>
                    <td><%= rs.getTimestamp("timestamp") %></td>
                    <td>
                        <form class="action-form" action="alert" method="post">
                            <input type="hidden" name="id" value="<%= rs.getInt("id") %>">
                            <input type="hidden" name="method" value="<%= rs.getString("method") %>">
                            <input type="hidden" name="uri" value="<%= rs.getString("uri") %>">
                            <input type="hidden" name="query" value="<%= rs.getString("query") %>">
                            <input type="hidden" name="ip" value="<%= rs.getString("ip_address") %>">
                            <button type="submit" class="<%= blocked ? "btn-blocked" : "btn-safe" %>">Notify Team</button>
                        </form>
                    </td>
                </tr>
                <%
                        }

                        if (!hasLogs) {
                %>
                    <tr>
                        <td colspan="8" class="no-logs">No request logs available at the moment.</td>
                    </tr>
                <%
                        }

                        rs.close();
                        stmt.close();
                        conn.close();
                    } catch (Exception e) {
                %>
                    <tr>
                        <td colspan="8" style="color:red; background-color:#fff;">Error fetching logs: <%= e.getMessage() %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <footer>
        © <%= new java.util.Date().getYear() + 1900 %> WAF Dashboard - All rights reserved.
    </footer>
</body>
</html>
