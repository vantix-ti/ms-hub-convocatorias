package cl.vantix.mshub.infrastructure.security;

import cl.vantix.mshub.domain.model.Usuario;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.expiration}") private long expiration;

    public String generateToken(Usuario usuario) {
        String roles = usuario.getRoles().stream()
                .map(r -> "ROLE_" + r.name()).collect(Collectors.joining(","));
        JwtBuilder builder = Jwts.builder()
                .subject(usuario.getEmail())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration));
        if (usuario.getInstitucionId() != null) {
            builder.claim("institucionId", usuario.getInstitucionId());
        }
        return builder.signWith(getKey()).compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            String subject = extractUsername(token);
            return subject.equals(username) && !isTokenExpired(token);
        } catch (JwtException e) { return false; }
    }

    private boolean isTokenExpired(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
