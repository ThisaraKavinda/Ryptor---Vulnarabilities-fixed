/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ControllerHelper;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Thisara Kavinda
 */
public class ManagerFilter implements Filter{
    
    @Override
    public void init(FilterConfig arg0) throws ServletException {}
  
    @Override
    public void doFilter(ServletRequest req,ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {
        
        if (req instanceof HttpServletRequest) {
            HttpServletRequest servletRequest = (HttpServletRequest)req;
            HttpSession session = servletRequest.getSession();
            Object userType = session.getAttribute("USER_TYPE");
            if (userType.equals("manager") || userType.equals("admin")) {
                chain.doFilter(req, resp);
            } else {
                req.getRequestDispatcher("Views/Admin/signin.jsp").forward(req, resp);
            }
        }
    }
    
    @Override
    public void destroy() {}
}


