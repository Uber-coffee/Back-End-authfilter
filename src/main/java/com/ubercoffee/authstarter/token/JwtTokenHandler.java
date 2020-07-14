package com.ubercoffee.authstarter.token;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log4j2
public class JwtTokenHandler implements TokenHandler {
    private final String idField;
    private final String rolesField;

    public JwtTokenHandler(String idField, String rolesField) {
        this.idField = idField;
        this.rolesField = rolesField;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getRoles(String token) throws TokenException {
        log.debug("getRoles token: " + token);

        Jwt<Header, Claims> claims = this.getClaims(token);
        List<String> roles = (List<String>) claims.getBody().get(rolesField, List.class);

        if (Objects.isNull(roles)) {
            return new ArrayList<>(0);
        }

        return roles;
    }

    @Override
    public Long getId(String token) throws TokenException {
        log.debug("getId token: " + token);

        Jwt<Header, Claims> claims = this.getClaims(token);

        Long id;
        try {
            id = Long.parseLong(claims.getBody().get(idField, String.class));
        } catch (NumberFormatException e) {
            throw new TokenException("Id field must be Long");
        }

        return id;
    }

    private Jwt<Header, Claims> getClaims(String token) throws TokenException {
        Jwt<Header, Claims> claims;

        token = this.removeSignature(token);

        log.debug("Stripped token: " + token);

        try {
            claims = Jwts.parser().parseClaimsJwt(token);
        } catch (ExpiredJwtException |
                UnsupportedJwtException |
                MalformedJwtException |
                SignatureException |
                IllegalArgumentException ex
        ) {
            ex.printStackTrace();
            throw new TokenException();
        }

        if (Objects.isNull(claims)) {
            throw new TokenException("Cannot find claims");
        }

        return claims;
    }

    private String removeSignature(String token) {
        int i = token.lastIndexOf('.');
        return token.substring(0, i + 1);
    }
}
