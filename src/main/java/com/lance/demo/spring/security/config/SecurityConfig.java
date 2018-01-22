package com.lance.demo.spring.security.config;

import com.lance.demo.spring.security.service.UserService;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.RequestCache;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private RequestCache requestCache = null;

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/css/**","/public/**", "/resources/**","/resources/public/**,/resources/static/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().loginPage("/login").successHandler(successHandler()).permitAll().and()
                .logout().permitAll();

    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler();
        handler.setUseReferer(true);
        return handler;
    }
}
