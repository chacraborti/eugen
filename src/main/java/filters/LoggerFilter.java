package filters;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class LoggerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        logDelimiter();
        logRequestHeaders(req);
        logRequestBody(req);

//        CopyPrintWriter writer = new CopyPrintWriter(resp.getWriter());
//        chain.doFilter(req, new HttpServletResponseWrapper(resp) {
//            @Override
//            public PrintWriter getWriter() {
//                return writer;
//            }
//        });
//        logResponseHeaders(resp);
//        lodResponseBody(writer);
    }

    private void logDelimiter(){
        LOGGER.info("===================================================================\n" +
                "=========================================================================================================");
    }

    private void logRequestHeaders(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REQUEST HEADERS of HTTP method  ").append(request.getMethod()).append(":\n");

        Enumeration<String> headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            String result = key + " = " + value +"\n";
            stringBuilder.append(result);
        }
        LOGGER.info(stringBuilder.toString());
    }

    private void logRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("REQUEST BODY of HTTP method ").append(request.getMethod()).append(":\n");

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String key = params.nextElement();
            String value = request.getParameter(key);
            stringBuilder.append(key + " = " + value + "\n");
        }
        LOGGER.info(stringBuilder.toString());
    }

    private void logResponseHeaders(HttpServletResponse response) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RESPONSE HEADERS:\n");

        response.getHeaderNames().stream().map(s -> s  + " = " + response.getHeader(s) + "\n").forEach(stringBuilder::append);

        LOGGER.info(stringBuilder.toString());
    }

    private void lodResponseBody(CopyPrintWriter writer) {
        LOGGER.info("RESPONSE BODY" + writer.getCopy());
    }
}
