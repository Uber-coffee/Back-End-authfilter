package com.ubercoffee.authstarter.filter;

import com.ubercoffee.authstarter.token.TokenException;
import com.ubercoffee.authstarter.token.TokenHandler;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class JwtTokenAuthFilter extends OncePerRequestFilter {
    private final String AUTHORIZATION_HEADER_TOKEN_PREFIX = "Bearer ";

    private final TokenHandler tokenHandler;

    public JwtTokenAuthFilter(TokenHandler tokenHandler) {
        this.tokenHandler = tokenHandler;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            String token = this.getTokenFromRequest(request);
            Long id = this.tokenHandler.getId(token);
            List<String> roles = this.tokenHandler.getRoles(token);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            id,
                            "",
                            roles.stream()
                                    .map(SimpleGrantedAuthority::new)
                                    .collect(Collectors.toSet())
                    )
            );

        } catch (TokenException ex) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid jwt");
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) throws TokenException {
        String authorization = request.getHeader("Authorization");

        if (Objects.isNull(authorization)) {
            throw new TokenException("Cannot find authorization header");
        }

        if (!authorization.startsWith(AUTHORIZATION_HEADER_TOKEN_PREFIX)) {
            throw new TokenException("Authorization field is malformed");
        }

        return authorization.substring(AUTHORIZATION_HEADER_TOKEN_PREFIX.length());
    }
}
