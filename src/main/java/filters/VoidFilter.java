package filters;

import javax.servlet.*;
import java.io.IOException;

public class VoidFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
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
