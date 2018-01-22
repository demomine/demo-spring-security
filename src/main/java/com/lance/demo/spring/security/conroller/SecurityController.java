package com.lance.demo.spring.security.conroller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *  当没有声明ViewResolver时，spring会注册一个默认的ViewResolver，就是JstlView的实例， 该对象继承自InternalResourceView。
 *  JstlView用来封装JSP或者同一Web应用中的其他资源，它将model对象作为request请求的属性值暴露出来, 并将该请求通过javax.servlet.RequestDispatcher转发到指定的URL.
 *  Spring认为， 这个view的URL是可以用来指定同一web应用中特定资源的，是可以被RequestDispatcher转发的。
 *  也就是说，在页面渲染(render)之前，Spring会试图使用RequestDispatcher来继续转发该请求。
 */
@Controller@RequestMapping("/")
public class SecurityController {
    @RequestMapping("/hello")
    public String hello(Model model) {
        model.addAttribute("msg", "message");
        return "index";
    }

    @RequestMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", "message");
        return "index";
    }
}
