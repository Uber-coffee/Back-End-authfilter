package com.ubercoffee.authstarter.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JwtTokenHandlerTest {
    private JwtTokenHandler tokenHandler;
    private String token;

    @BeforeAll
    void init() {
        this.tokenHandler = new JwtTokenHandler("sub", "roles");

        this.token = Jwts.builder()
                .setSubject("1")
                .setIssuedAt(new Date(new Date().getTime() - 60_000))
                .setExpiration(new Date(new Date().getTime() + 60_000))
                .claim("roles", List.of("ROLE_ADMIN"))
                .signWith(SignatureAlgorithm.HS512, "MTIz")
                .compact();
    }

    @Test
    void getRoles() throws TokenException {
        List<String> roles = this.tokenHandler.getRoles(token);
        assertIterableEquals(List.of("ROLE_ADMIN"), roles);
    }

    @Test
    void getId() throws TokenException {
        Long id = this.tokenHandler.getId(token);
        assertEquals(1, id);
    }
}