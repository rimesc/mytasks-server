package uk.co.zoneofavoidance.my.tasks.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * Adds the XSRF-TOKEN cookie for AngularJS. See
 * <a href= "https://spring.io/guides/tutorials/spring-security-and-angular-js">
 * Spring Security and Angular JS </a>.
 */
public class CsrfHeaderFilter extends OncePerRequestFilter {
   @Override
   protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
      final CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
      if (csrf != null) {
         Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
         final String token = csrf.getToken();
         if (cookie == null || token != null && !token.equals(cookie.getValue())) {
            cookie = new Cookie("XSRF-TOKEN", token);
            cookie.setPath(request.getContextPath());
            response.addCookie(cookie);
         }
      }
      filterChain.doFilter(request, response);
   }
}