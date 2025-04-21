package com.myapp.webappfirewall.resources;

import com.myapp.webappfirewall.utils.DBLogger;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Security Filter for detecting SQL Injection and XSS attacks and logging all requests.
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

    private static final Pattern SQLI_PATTERN = Pattern.compile(
            "('.+--)|(--|#|%23|%2D%2D)|(;)|(%7C)|(/\\.?\\*/)|" +
            "(\\b(SELECT|UPDATE|DELETE|INSERT|DROP|UNION|ALTER|CREATE|WHERE|HAVING|OR|AND)\\b)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern XSS_PATTERN = Pattern.compile(
    "(<script.*?>.*?</script>)|((javascript:|data:)[^\\s]*)|(<.*?on\\w+\\s*=\\s*['\"].*?['\"])",
    Pattern.CASE_INSENSITIVE | Pattern.DOTALL
);


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("✅ SecurityFilter Initialized: Protecting against SQLi & XSS");
    }

@Override
public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    String method = req.getMethod();
    String uri = req.getRequestURI();
    String query = req.getQueryString();
    String ip = req.getHeader("X-Forwarded-For");
if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
    // X-Forwarded-For can contain multiple IPs — the first one is the original client
    ip = ip.split(",")[0].trim();
} else {
    ip = request.getRemoteAddr();
}


    boolean isBlocked = false;

    // Skip security checks for /alert endpoint
    if (uri.endsWith("/alert")) {
        chain.doFilter(request, response);
        return;
    }

    try {
        // Scan query string
        if (query != null) {
            String decodedQuery = URLDecoder.decode(query, StandardCharsets.UTF_8);
            if (SQLI_PATTERN.matcher(decodedQuery).find() || XSS_PATTERN.matcher(decodedQuery).find()) {
                isBlocked = true;
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "🚨 Malicious Query Detected! Access Denied.");
                System.out.println("[SECURITY ALERT] Query Blocked: " + uri + "?" + decodedQuery);
                return;
            }
        }

        // Scan request parameters
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);

            if (paramValue != null) {
                String decodedValue = URLDecoder.decode(paramValue, StandardCharsets.UTF_8);
                if (SQLI_PATTERN.matcher(decodedValue).find() || XSS_PATTERN.matcher(decodedValue).find()) {
                    isBlocked = true;
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "🚨 Malicious Input Detected! Access Denied.");
                    System.out.println("[SECURITY ALERT] Param Blocked: " + uri + " | Param: " + paramName);
                    return;
                }
            }
        }

        chain.doFilter(request, response); // continue normal flow

    } finally {
        // 🔐 Log request to the DB
        DBLogger.logRequest(method, uri, query, ip, isBlocked);
    }
}


    @Override
    public void destroy() {
        System.out.println("SecurityFilter destroyed.");
    }
}
