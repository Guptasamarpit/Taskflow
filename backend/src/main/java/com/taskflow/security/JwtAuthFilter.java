package com.taskflow.security;

import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    final JwtService jwt;

    public JwtAuthFilter(JwtService j) {
        jwt = j;
    }

    protected void doFilterInternal(HttpServletRequest r, HttpServletResponse s, FilterChain c)
            throws ServletException, IOException {
        String h = r.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            String t = h.substring(7);
            if (jwt.valid(t)) {
                String e = jwt.email(t);
                Authentication a = new UsernamePasswordAuthenticationToken(e, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(a);
            }
        }
        c.doFilter(r, s);
    }
}
