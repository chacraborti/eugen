package main.java.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class LoggerFilter implements Filter {

    private static final Logger LOGGER = Logger.getLogger("LOGGER");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        logRequestHeaders(req);
        logRequestBody(req);

        HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(resp) {
            @Override
            public PrintWriter getWriter() throws IOException {
                return new CopyPrintWriter(resp.getWriter());
            }
        };
        chain.doFilter(request, wrappedResponse);

        logResponseHeaders(wrappedResponse);
        logResponseBody(wrappedResponse);
    }

    private void logRequestHeaders(HttpServletRequest request) {
        LOGGER.info("METHOD: " + request.getMethod());
        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            LOGGER.info(request.getRemoteAddr() + "REQUEST HEADERS {" + key + "=" + value + "}");
        }
    }

    private void logRequestBody(HttpServletRequest request) {
        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String key = params.nextElement();
            String value = request.getParameter(key);
            LOGGER.info(request.getRemoteAddr() + "REQUEST PARAMS: {" + key + "=" + value + "}");
        }
    }

    private void logResponseHeaders(HttpServletResponse response) {
        response.getHeaderNames()
                .forEach(e -> LOGGER.info("RESPONSE HEADERS: " + e));
    }

    private void logResponseBody(HttpServletResponse response) throws IOException {
        String responseBody = ((CopyPrintWriter)response.getWriter()).getCopy();
        LOGGER.info("RESPONSE BODY: " + responseBody);
    }
}
