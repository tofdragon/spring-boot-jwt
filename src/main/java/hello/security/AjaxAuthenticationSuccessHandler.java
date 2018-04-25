package hello.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import hello.model.TokenUserDTO;
import hello.web.utils.JwtFactory;

public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
        throws IOException, ServletException {

        TokenUserDTO tokenUserDTO = new TokenUserDTO();
        tokenUserDTO.setId("user");
        tokenUserDTO.setUsername("user");
        tokenUserDTO.setEmail("284312195@qq.com");

        String token = JwtFactory.create(tokenUserDTO);

        // 写入cookie,这样页面访问会自带cookie
        Cookie cookie = new Cookie(JwtFactory.TOKEN_NAME, token);
        cookie.setPath("/");
        response.addCookie(cookie);

        response.setStatus(HttpServletResponse.SC_CREATED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println(token);
        response.getWriter().flush();
        response.getWriter().close();
    }


}
