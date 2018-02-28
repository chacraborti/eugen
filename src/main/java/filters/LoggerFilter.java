package filters;

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
        LOGGER.info("===================================================================\n" +
                "=========================================================================================================");
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
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REQUEST HEADERS of HTTP method " + request.getMethod() + ":\n");

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            stringBuilder.append(key + " = " + value +"\n");
        }
        LOGGER.info(stringBuilder);
    }

    private void logRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REQUEST BODY of HTTP method " + request.getMethod() + ":\n");

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String key = params.nextElement();
            String value = request.getParameter(key);
            stringBuilder.append(key + " = " + value + "\n");
        }
        LOGGER.info(stringBuilder);
    }

    private void logResponseHeaders(HttpServletResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RESPONSE HEADERS:\n");

        response.getHeaderNames().stream().map(s -> s  + " = " + response.getHeader(s) + "\n").forEach(stringBuilder::append);

        LOGGER.info(stringBuilder);
    }

    private void logResponseBody(HttpServletResponse response) throws IOException {
        String responseBody = ((CopyPrintWriter)response.getWriter()).getCopy();
        LOGGER.info("RESPONSE BODY: " + responseBody);
    }
}
