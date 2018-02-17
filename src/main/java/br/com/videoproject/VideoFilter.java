package br.com.videoproject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(urlPatterns = {"/protected/*"})
public class VideoFilter implements Filter {

    private static final String ERROR_PAGE = "/error.jsp";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String token = servletRequest.getParameter("token");

        if(null != token && !token.isEmpty()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletRequest.setAttribute("message", "Access denied!");
            RequestDispatcher dispatcher = servletRequest.getRequestDispatcher(ERROR_PAGE);
            dispatcher.include(servletRequest, servletResponse);
        }
    }
}
