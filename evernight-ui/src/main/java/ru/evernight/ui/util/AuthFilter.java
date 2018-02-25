package ru.evernight.ui.util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.jsf"})
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws ServletException {
        try {
            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession ses = reqt.getSession(true);

            String reqURI = reqt.getRequestURI();
            if (ses.getAttribute("username") != null) {
                if (reqURI.contains("/login.jsf")) {
                    resp.sendRedirect(reqt.getContextPath() + "/pages/hall.jsf");
                } else {
                    chain.doFilter(request, response);
                }
            } else if (reqURI.contains("/login.jsf")) {
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect(reqt.getContextPath() + "/pages/login.jsf");
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {

    }
}