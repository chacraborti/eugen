package main.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class LoggerFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger("LOGGER");

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        logRequestHeaders(req);
        LOGGER.info("Method:\t"+req.getMethod());
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
        LOGGER.info("request body" + writer.getCopy());
    }

    private void logRequestHeaders(HttpServletRequest request) {

        Map<String, String> map = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }

        map.entrySet().stream().forEach(e -> LOGGER.info("request header: " + e.toString()));
    }


    private void logRequestBody(HttpServletRequest req) {

        Enumeration<String> params = req.getParameterNames();
        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = req.getParameter(name);
            LOGGER.info(req.getRemoteAddr() + "request params: {" + name + "=" + value + "}");
        }
    }

    private void logResponseHeaders(HttpServletResponse response) {

        response.getHeaderNames().stream().forEach(e -> LOGGER.info("response header: " + e.toString()));
    }

    @Override
    public void destroy() {

    }
}
