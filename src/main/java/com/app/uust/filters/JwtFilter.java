package com.app.uust.filters;

import com.app.uust.services.AdminService;
import com.app.uust.services.EmployeeService;
import com.app.uust.utils.JwtUtil;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private AdminService adminService;

  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    @NotNull HttpServletResponse response,
    @NotNull FilterChain filterChain
  )
    throws ServletException, IOException {
    String authHeader = request.getHeader("Authorization");

    String username = null;
    String jwtToken = null;
    String type = null;

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      jwtToken = authHeader.substring(7);
      username = jwtUtil.extractUsername(jwtToken);
      type = jwtUtil.extractType(jwtToken);
    }

    if (
      username != null &&
      SecurityContextHolder.getContext().getAuthentication() == null
    ) {
      UserDetails userDetails = null;
      if (type.equals("employee")) {
        userDetails = this.employeeService.loadUserByUsername(username);
      } else {
        System.out.println("HIHELLO SELECTED");
        userDetails = this.adminService.loadUserByUsername(username);
      }
      if (jwtUtil.validateToken(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
        usernamePasswordAuthenticationToken.setDetails(
          new WebAuthenticationDetailsSource().buildDetails(request)
        );
        SecurityContextHolder
          .getContext()
          .setAuthentication(usernamePasswordAuthenticationToken);
      }
    }

    request.setAttribute("username", username);
    request.setAttribute("type", type);
    filterChain.doFilter(request, response);
  }
}
