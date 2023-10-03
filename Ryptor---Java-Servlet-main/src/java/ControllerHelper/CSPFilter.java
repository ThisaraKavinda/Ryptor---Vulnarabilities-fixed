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
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Thisara Kavinda
 */
public class CSPFilter implements Filter {
	
	public static final String POLICY = "default-src *; style-src 'self' 'unsafe-inline'; script-src 'self' 'unsafe-inline' 'unsafe-eval' http://cdnjs.cloudflare.com; script-src-elem 'self' 'unsafe-inline' 'unsafe-eval' http://cdnjs.cloudflare.com ";
        
        @Override
        public void init(FilterConfig arg0) throws ServletException {}

	@Override
        public void doFilter(ServletRequest request, ServletResponse response,
                             FilterChain chain) throws IOException, ServletException {
            if (response instanceof HttpServletResponse) {
                    ((HttpServletResponse)response).setHeader("Content-Security-Policy", CSPFilter.POLICY);
                    ((HttpServletResponse)response).setHeader("Access-Control-Allow-Origin","http://localhost:8080");
                    ((HttpServletResponse)response).setHeader("X-Frame-Options","deny");
                    chain.doFilter(request, response);
            }
        }

	@Override
        public void destroy() {}

}
