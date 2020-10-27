package SpringReactDemo.SpringReactDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class PageController {

    @GetMapping("/test")
    public String test() {
        return "Hello World!!";
    }

    @GetMapping("/time")
    public String time() {
        return "Server time is " + new Date();
    }

    @GetMapping("/index")
    public String index() {
        return "index.html";
    }

}
