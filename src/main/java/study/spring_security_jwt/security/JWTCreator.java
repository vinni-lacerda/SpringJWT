package study.spring_security_jwt.security;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class JWTCreator {

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    public static String create(String prefix, String key, JWTObject jwtObject) {

        Key secretKey = Keys.hmacShaKeyFor(key.getBytes());

        String token = Jwts.builder()
                .setSubject(jwtObject.getSubject())
                .setIssuedAt(jwtObject.getIssuedAt())
                .setExpiration(jwtObject.getExpiration())
                .claim(ROLES_AUTHORITIES, checkRoles(jwtObject.getRoles()))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();

        return prefix + " " + token;
    }

    public static JWTObject create(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException,
            MalformedJwtException, SignatureException {

        JWTObject object = new JWTObject();

        token = token.replace(prefix + " ", "");

        Key secretKey = Keys.hmacShaKeyFor(key.getBytes());

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        object.setSubject(claims.getSubject());
        object.setIssuedAt(claims.getIssuedAt());
        object.setExpiration(claims.getExpiration());
        object.setRoles((List<String>) claims.get(ROLES_AUTHORITIES));

        return object;
    }

    private static List<String> checkRoles(List<String> roles) {
        return roles.stream()
                .map(role -> "ROLE_" + role.replace("ROLE_", ""))
                .collect(Collectors.toList());
    }
}