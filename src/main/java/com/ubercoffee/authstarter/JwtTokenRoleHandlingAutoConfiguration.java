package com.ubercoffee.authstarter;

import com.ubercoffee.authstarter.filter.JwtTokenAuthFilter;
import com.ubercoffee.authstarter.token.JwtTokenHandler;
import com.ubercoffee.authstarter.token.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.validation.ObjectError;

import java.util.Objects;

@Configuration
@ConditionalOnProperty(
        prefix="auth.token",
        name = {
                "enabled",
                "id-field",
                "roles-field"
        }
        )
public class JwtTokenRoleHandlingAutoConfiguration {
    @Configuration
    @EnableWebSecurity
    @EnableConfigurationProperties(RoleHandlingProperties.class)
    protected static class RoleHandlingConfigurationAdapter extends WebSecurityConfigurerAdapter {
        private final RoleHandlingProperties properties;

        private final TokenHandler tokenHandler;

        public RoleHandlingConfigurationAdapter(RoleHandlingProperties properties) {
            this.properties = properties;
            this.tokenHandler = new JwtTokenHandler(
                    properties.getIdField(),
                    properties.getRolesField()
            );
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            if (Objects.isNull(properties.getWhitelist())) {
                return;
            }

            if (properties.getWhitelist().size() == 0) {
                return;
            }

            web.ignoring().antMatchers(properties.getWhitelist().toArray(String[]::new));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (Objects.isNull(properties.getEnabled()) || !properties.getEnabled()) {
                return;
            }

            http
                    .csrf()
                    .disable();

            http
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http
                    .antMatcher("/**")
                    .addFilterBefore(new JwtTokenAuthFilter(tokenHandler), UsernamePasswordAuthenticationFilter.class)
                    .authorizeRequests()
                    .anyRequest()
                    .authenticated();
        }
    }
}
