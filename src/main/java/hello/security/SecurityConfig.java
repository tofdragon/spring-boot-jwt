package hello.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import hello.web.utils.JwtFactory;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .enableSessionUrlRewriting(false)
                .and()
                .authorizeRequests()
                .antMatchers("/css/**", "/index").permitAll()
                .anyRequest().authenticated()

                .and()
                .formLogin()
                .loginProcessingUrl("/api/login")
                .loginPage("/login.html")
                .permitAll()
                .successHandler(new AjaxAuthenticationSuccessHandler())
                .permitAll()

                .and()
                .logout()
                .logoutUrl("/api/logout")
                .deleteCookies(JwtFactory.TOKEN_NAME)
                .permitAll()

                .and()
                .csrf().disable()
                .addFilterBefore(new VerifyTokenFilter(), UsernamePasswordAuthenticationFilter.class);


    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("{noop}user").roles("USER");
    }
}
