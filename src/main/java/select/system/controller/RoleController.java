package select.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeyuting
 * @create 2021/1/27
 */
@RestController
@RequestMapping("/sys/admin")
public class RoleController {
    @GetMapping("/greeting")
    public String adminGreeting() {
        return "Hello,World!admin";
    }

    @GetMapping("/login")
    public String adminLogin() {
        return "login!admin";
    }
}
