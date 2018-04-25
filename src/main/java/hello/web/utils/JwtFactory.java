package hello.web.utils;

import java.util.List;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;

import hello.model.TokenUserDTO;
import hello.security.TokenUserAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtFactory {

    public static String create(TokenUserDTO userDTO) {

        return Jwts.builder()
                //.setExpiration(new Date(System.currentTimeMillis()) + )
                .setSubject(userDTO.getUsername())
                .claim("id", userDTO.getId())
                .claim("avatar", userDTO.getAvatar())
                .claim("email", userDTO.getEmail())
                .claim("roles", userDTO.getRoles())
                .signWith(SignatureAlgorithm.HS256, "w-oasis123456")
                .compact();
    }

    /**
     * 从token中取出用户
     */
    public static TokenUserDTO parse(String token) {
        System.err.println("token*****************" + token);
        Claims claims = Jwts.parser()
                .setSigningKey("w-oasis123456")
                .parseClaimsJws(token)
                .getBody();

        TokenUserDTO userDTO = new TokenUserDTO();
        userDTO.setId(claims.getId());
        userDTO.setAvatar(claims.get("avatar",String.class));
        userDTO.setUsername(claims.get("username",String.class));
        userDTO.setEmail(claims.get("email",String.class));
        userDTO.setRoles((List<String>) claims.get("roles"));
        return userDTO;
    }

    public static final String TOKEN_NAME = "jwt-token";

    public static Optional<Authentication> verifyToken(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_NAME);

        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null && cookies.length > 0) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(TOKEN_NAME)) {
                        token = cookie.getValue();
                    }
                }
            }
        }

        if (token != null && !token.isEmpty()){
            final TokenUserDTO user = JwtFactory.parse(token.trim());
            if (user != null) {
                return Optional.of(new TokenUserAuthentication(user, true));
            }
        }
        return Optional.empty();
    }
}
