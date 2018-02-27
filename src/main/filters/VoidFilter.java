package main.filters;

import org.apache.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

public class VoidFilter implements Filter {

    Logger logger;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.logger = Logger.getLogger("logger");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        response.getWriter().write("xxxxxxxxxx");
    }

    @Override
    public void destroy() {

    }
}
