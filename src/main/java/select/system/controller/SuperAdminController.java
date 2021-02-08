package select.system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import select.base.Result;
import select.constants.BaseEnums;
import select.system.dto.User;
import select.system.service.UserService;
import select.util.PageBean;
import select.util.Results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yeyuting
 * @create 2021/2/3
 */
@RestController
@RequestMapping("/sys/superAdmin")
public class SuperAdminController {
    @Autowired
    UserService userService ;

    @GetMapping("/selectByName")
    public Result selectByName(@RequestParam("userName") String username){
        return Results.successWithData(userService.selectByName(username) , BaseEnums.SUCCESS.code()) ;
    }

    @GetMapping("/selectById")
    public Result selectByName(@RequestParam("id") int id ){
        return Results.successWithData(userService.selectById(id) , BaseEnums.SUCCESS.code()) ;
    }

    @GetMapping("/selectAll")
    public Result selectAll(){
        return Results.successWithData(userService.selectAll() , BaseEnums.SUCCESS.code()) ;
    }
    @PostMapping("/insertOne")
    public Result insertOne(@RequestBody User user){
        return Results.successWithData(userService.insertOne(user) , BaseEnums.SUCCESS.code()) ;

    }
    @PostMapping("/insertMany")
    public Result insertMany(@RequestBody List<User> userList){
        return Results.successWithData(userService.insertMany(userList) , BaseEnums.SUCCESS.code()) ;
    }

    @PostMapping("/updateOne")
    public Result updateOne(@RequestBody User user){
        return Results.successWithData(userService.updateOne(user) , BaseEnums.SUCCESS.code()) ;
    }
    @PostMapping("/deleteById")
    public Result deleteById(@RequestParam int id){
        return Results.successWithData(userService.deleteById(id) , BaseEnums.SUCCESS.code()) ;
    }

    @GetMapping("/SelectByStartIndexAndPageSize")
    public Result SelectByStartIndexAndPageSize(
            @RequestParam("currentPage") int currentPage, @RequestParam("pageSize") int pageSize){
        List<User> userList = userService.SelectByStartIndexAndPageSize
                ((currentPage-1)*pageSize , pageSize) ;
        return Results.successWithData(userList , BaseEnums.SUCCESS.code()) ;

    }

    @GetMapping("SelectByMap")
    public Result SelectByMap(@RequestParam("currentPage")int currentPage , @RequestParam("pageSize") int pageSize){
        Map<String ,Object> map = new HashMap<>() ;
        map.put("startIndex" , (currentPage-1)*pageSize) ;
        map.put("pageSize" , pageSize) ;

        List<User> userList = userService.selectByMap(map) ;

        return Results.successWithData(userList , BaseEnums.SUCCESS.code()) ;
    }

    @GetMapping("SelectByPageBean")
    public Result SelectByPageBean(@RequestParam("currentPage") int currentPage , @RequestParam("pageSize") int pageSize){
        PageBean pageBean = new PageBean() ;
        pageBean.setStartIndex((currentPage-1)*pageSize);
        pageBean.setPageSize(pageSize);
        return Results.successWithData(userService.SelectByPageBean(pageBean) , BaseEnums.SUCCESS.code()) ;

    }

    @GetMapping("SelectByLike")
    public Result SelectByLike(@RequestParam("keywords") String keywords,
                               @RequestParam("currentPage") int currentPage,
                               @RequestParam("pageSize") int pageSize){
        Map<String,Object> map = new HashMap<>() ;
        map.put("keywords" , keywords) ;
        map.put("startIndex" , (currentPage-1)*pageSize) ;
        map.put("pageSize" , pageSize) ;
        userService.selectByLike(map) ;
        return Results.successWithData(userService.selectByLike(map) , BaseEnums.SUCCESS.code()) ;
    }


}
