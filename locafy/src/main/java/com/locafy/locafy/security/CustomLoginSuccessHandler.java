package com.locafy.locafy.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        String redirectURL = request.getContextPath();
        boolean hasValidRole = false;

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            if (role.equals("ROLE_LOCAL")) {
                redirectURL = "/locals-home";
                hasValidRole = true;
                break;
            } else if (role.equals("ROLE_BUSINESS_OWNER")) {
                redirectURL = "/business-owner-home";
                hasValidRole = true;
                break;
            }
        }

        /*if (!hasValidRole) {
            // Clear authentication
            SecurityContextHolder.clearContext();

            // Invalidate session
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }

            // Redirect to login page with error
            response.sendRedirect("/login?error=missing_role");
            return;
        }*/
        System.out.println("Redirect URL: " + redirectURL); // Debugging line

        response.sendRedirect(redirectURL);
    }
}
