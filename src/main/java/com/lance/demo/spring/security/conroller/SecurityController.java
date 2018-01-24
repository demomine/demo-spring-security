package com.lance.demo.spring.security.conroller;

import com.lance.demo.spring.security.auth.AuthReq;
import com.lance.demo.spring.security.auth.AuthRsp;
import com.lance.demo.spring.security.service.UserService;
import com.lance.demo.spring.security.util.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *  当没有声明ViewResolver时，spring会注册一个默认的ViewResolver，就是JstlView的实例， 该对象继承自InternalResourceView。
 *  JstlView用来封装JSP或者同一Web应用中的其他资源，它将model对象作为request请求的属性值暴露出来, 并将该请求通过javax.servlet.RequestDispatcher转发到指定的URL.
 *  Spring认为， 这个view的URL是可以用来指定同一web应用中特定资源的，是可以被RequestDispatcher转发的。
 *  也就是说，在页面渲染(render)之前，Spring会试图使用RequestDispatcher来继续转发该请求。
 */
@RestController@RequestMapping("/auth")
public class SecurityController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/login")
    public AuthRsp authentication(@RequestBody @Valid AuthReq authReq) {
        final UserDetails userDetails = userService.loadUserByUsername(authReq.getUsername());
        if (userDetails.getPassword() == null) {
            throw new UsernameNotFoundException("No user exists");
        }

        UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(authReq.getUsername(), authReq.getCredential());
        authenticationManager.authenticate(authenticate);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        final String token = jwtTokenService.generateToke(userDetails);
        // Return the token
        return new AuthRsp(token);
    }
}
