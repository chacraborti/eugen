package filters;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.logging.Logger;

public class LoggerFilter implements Filter {

    private ServletContext context;
    Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.context = filterConfig.getServletContext();
        this.logger = Logger.getLogger("logger");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        Enumeration<String> params = req.getParameterNames();
        while(params.hasMoreElements()){
            String name = params.nextElement();
            String value = req.getParameter(name);
            logger.warning(req.getRemoteAddr() + "::Request Params::{"+name+"="+value+"}");
        }

        final CopyPrintWriter writer = new CopyPrintWriter(response.getWriter());

        chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse) response) {
            @Override public PrintWriter getWriter() {
                return writer;
            }
        });
        logger.warning(writer.getCopy());
    }

    @Override
    public void destroy() {

    }
}
