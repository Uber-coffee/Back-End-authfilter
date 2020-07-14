package com.ubercoffee.authstarter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.token")
@Data
public class RoleHandlingProperties {
    /**
     * enable starter
     */
    private Boolean enabled;

    /**
     * field in jwt token which contains user id
     */
    private String idField;

    /**
     * field in jwt token which contains roles list
     */
    private String rolesField;
}
