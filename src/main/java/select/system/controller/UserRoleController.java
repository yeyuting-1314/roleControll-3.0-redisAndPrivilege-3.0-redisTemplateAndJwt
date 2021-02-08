package select.system.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yeyuting
 * @create 2021/1/27
 */
@RestController
@RequestMapping("/sys/superAdmin")
public class UserRoleController {

    @GetMapping("/greeting")
    public String superAdminGreeting() {
        return "Hello,World ! superAdmin";
    }

    @GetMapping("/login")
    public String superAdminLogin() {
        return "login ! superAdmin";
    }
}
