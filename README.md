## Usage

Add maven dependency:
```
<dependency>
    <groupId>com.ubercoffee</groupId>
    <artifactId>auth-starter</artifactId>
    <version>0.0.4</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

Enable security annotations processing
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