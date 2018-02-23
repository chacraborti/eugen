package main.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class LoggerFilter implements Filter {

    Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.logger = Logger.getLogger("logger");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        logRequestHeaders(req);
        logRequestBody(req);
        logResponseHeaders(resp);
        logResponseBody(request, resp, chain);

    }

    private void logResponseBody(ServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final CopyPrintWriter writer = new CopyPrintWriter(response.getWriter());
        chain.doFilter(request, new HttpServletResponseWrapper(response) {
            @Override
            public PrintWriter getWriter() {
                return writer;
            }
        });
        logger.warning("request body" + writer.getCopy());
    }

    private void logRequestHeaders(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        map.entrySet().stream().forEach(e -> logger.warning("request header: " + e.toString()));
    }


    private void logRequestBody(HttpServletRequest req) {

        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = req.getParameter(name);
            logger.warning(req.getRemoteAddr() + "request params: {" + name + "=" + value + "}");
        }
    }

    private void logResponseHeaders(HttpServletResponse response) {

        response.getHeaderNames().stream().forEach(e -> logger.warning("response header: " + e.toString()));
    }

    @Override
    public void destroy() {

    }
}
