## Usage

In your pom.xml add maven dependency:
```
<dependency>
    <groupId>com.ubercoffee</groupId>
    <artifactId>auth-starter</artifactId>
    <version>0.0.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```
and maven repository:
```
<repositories>
    <repository>
        <id>repsy</id>
        <url>https://repo.repsy.io/mvn/kotique/default</url>
    </repository>
</repositories>
```

In your `application.yml` add:
```yaml
auth:
  token:
    enabled: true
    id-field: sub
    roles-field: roles
    whitelist:
      - /configuration/**
      - /swagger-resources/**
      - /swagger-ui.html
      - /v2/api-docs
      - /webjars/**
```

Enable security annotations processing:
```
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
```

Usage:
```
@RequestMapping("/test")
@PreAuthorize("hasAuthority('ROLE_SELLER')")
public ResponseEntity<String> test(Authentication authentication) {
    long id = (long) authentication.getPrincipal();
    ...
}
```