package hello.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import hello.web.utils.JwtFactory;
import io.jsonwebtoken.JwtException;


public class VerifyTokenFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<Authentication> authentication = JwtFactory.verifyToken(request);
            SecurityContextHolder.getContext().setAuthentication(authentication.orElse(null));
        } catch (JwtException e) {
            LOGGER.error(e.getMessage());
        } finally {
            filterChain.doFilter(request,response);
        }
    }
}
