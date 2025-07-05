package com.example.api_demo.Controller;



import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class welcomeController {
    @GetMapping("/")
    public String getMethodName(HttpServletRequest request) {

        return "welcome";
    }


}
