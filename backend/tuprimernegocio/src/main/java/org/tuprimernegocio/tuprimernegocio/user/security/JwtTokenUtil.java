package org.tuprimernegocio.tuprimernegocio.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private String SECRET_KEY;
    private final Map<String, List<TokenInfo>> activeTokensByUser = new ConcurrentHashMap<>();

    public static class TokenInfo {
        private final String token;
        private final String customSecret;

        TokenInfo(String token, String customSecret) {
            this.token = token;
            this.customSecret = customSecret;
        }

        public String getToken() {
            return token;
        }

        public String getCustomSecret() {
            return customSecret;
        }
    }

    public JwtTokenUtil(@Value("${jwt.secret:defaultSecret}") String SECRET_KEY) {
        this.SECRET_KEY = SECRET_KEY;
    }

    public String generateTokenWithCustomSecret(String username, String customSecret) {
    Map<String, Object> claims = new HashMap<>();
    String token = createToken(claims, username, customSecret);
    // Añadir el token a la lista del usuario
    activeTokensByUser.computeIfAbsent(username, k -> new ArrayList<>())
                      .add(new TokenInfo(token, customSecret));
    return token;
}

private void cleanupExpiredTokens(String username) {
    List<TokenInfo> tokens = activeTokensByUser.get(username);
    if (tokens != null) {
        tokens.removeIf(tokenInfo -> isTokenExpired(tokenInfo.getToken(), tokenInfo.getCustomSecret()));
        if (tokens.isEmpty()) {
            activeTokensByUser.remove(username);
        }
    }
}


    public String extractUsername(String token, String customSecret) {
        return extractClaim(token, Claims::getSubject, customSecret);
    }

    public Date extractExpiration(String token, String customSecret) {
        return extractClaim(token, Claims::getExpiration, customSecret);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver, String customSecret) {
        final Claims claims = extractAllClaims(token, customSecret);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, String customSecret) {
        return Jwts.parser().setSigningKey(customSecret).parseClaimsJws(token).getBody();
    }

   

    private String createToken(Map<String, Object> claims, String subject, String secret) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // 10 horas de validez
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public Boolean validateTokenWithCustomSecret(String token, String username) {
        List<TokenInfo> userTokens = activeTokensByUser.getOrDefault(username, new ArrayList<>());
        for (TokenInfo tokenInfo : userTokens) {
            if (tokenInfo.getToken().equals(token) && !isTokenExpired(token, tokenInfo.getCustomSecret())) {
                return true;
            }
        }
         cleanupExpiredTokens(username);
        return false;
    }
    

    private Boolean isTokenExpired(String token, String customSecret) {
        return extractExpiration(token, customSecret).before(new Date());
    }

    public String getEmailFromToken(String token, String customSecret) {
        final Claims claims = extractAllClaims(token, customSecret);
        return claims.get("email", String.class);
    }

    public String extractUsernameUsingDefaultKey(String token) {
        for (Map.Entry<String, List<TokenInfo>> entry : activeTokensByUser.entrySet()) {
            for (TokenInfo tokenInfo : entry.getValue()) {
                if (tokenInfo.getToken().equals(token)) {
                    try {
                        return Jwts.parser().setSigningKey(tokenInfo.getCustomSecret()).parseClaimsJws(token).getBody().getSubject();
                    } catch (Exception e) {
                        // En caso de error en la extracción del nombre de usuario (por ejemplo, si el token no es válido)
                        return null;
                    }
                }
            }
        }
        return null; // Si no se encuentra el token
    }
    
    
    public Boolean validateToken(String token) {
        return validateTokenWithCustomSecret(token, SECRET_KEY);
    }

    public boolean isActiveTokenForUser(String token, String username) {
        List<TokenInfo> userTokens = activeTokensByUser.getOrDefault(username, new ArrayList<>());
        for (TokenInfo tokenInfo : userTokens) {
            if (tokenInfo.getToken().equals(token) && !isTokenExpired(token, tokenInfo.getCustomSecret())) {
                return true;
            }
        }
        return false;
    }
    
    public void invalidatePreviousTokens(String username) {
        activeTokensByUser.remove(username);
    }

    //Password reset

    public String generatePasswordResetToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "passwordReset");
        
        // Duración más larga para el token de restablecimiento
        long expirationTime = 1000 * 60 * 60 * 24; // 24 horas
    
        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    
        activeTokensByUser.computeIfAbsent(username, k -> new ArrayList<>())
                          .add(new TokenInfo(token, SECRET_KEY));
    
        return token;
    }
    public boolean validatePasswordResetToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
            boolean isExpired = claims.getExpiration().before(new Date());
            boolean isResetToken = "passwordReset".equals(claims.get("purpose"));
    
            if (isExpired || !isResetToken) {
                return false;
            }
    
            String username = claims.getSubject();
            return isActiveTokenForUser(token, username);
        } catch (Exception e) {
            return false;
        }
    }
    public void invalidatePasswordResetToken(String token) {
        String username = extractUsernameUsingDefaultKey(token);
        activeTokensByUser.computeIfPresent(username, (k, v) -> {
            v.removeIf(tokenInfo -> tokenInfo.getToken().equals(token));
            return v.isEmpty() ? null : v;
        });
    }
    
    
}
