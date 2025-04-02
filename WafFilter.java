package com.waf;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class WafFilter implements Filter {

    // Improved Regex for better detection of attacks
    private static final Pattern SQL_INJECTION_PATTERN = Pattern.compile("(?i)(union|select|insert|delete|update|drop|--|;|\\*|\\bOR\\b|\\bAND\\b|exec|execute|concat|char\\()");
    private static final Pattern XSS_PATTERN = Pattern.compile("(?i)(<script|</script>|javascript:|onerror|onload|alert\\(|document\\.cookie|document\\.write|window\\.location)");
    private static final Pattern CSRF_PATTERN = Pattern.compile("(?i)(csrf_token|_csrf)");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🔥 Web Application Firewall Initialized");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Extract all parameters to check
        Enumeration<String> paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = req.getParameter(paramName);

            if (isMalicious(paramValue)) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "🚫 Blocked by Web Application Firewall! 🚨");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private boolean isMalicious(String input) {
        if (input == null) return false;
        return SQL_INJECTION_PATTERN.matcher(input).find() || 
               XSS_PATTERN.matcher(input).find() || 
               CSRF_PATTERN.matcher(input).find();
    }

    @Override
    public void destroy() {
        System.out.println("🛑 Web Application Firewall Stopped");
    }
}
