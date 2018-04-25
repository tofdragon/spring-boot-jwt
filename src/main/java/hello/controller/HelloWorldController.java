package hello.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *  hello world控制器
 */
@Controller
public class HelloWorldController {

    @RequestMapping("/api/helloWorld")
    @ResponseBody
    String helloWorld() {
        return "Hello World!";
    }
}
