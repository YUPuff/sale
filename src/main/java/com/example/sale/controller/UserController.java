package com.example.sale.controller;



import com.example.sale.annotation.NoAuth;
import com.example.sale.common.Result;
import com.example.sale.constant.GroupConstants;
import com.example.sale.dto.DataDTO;
import com.example.sale.dto.UserDTO;
import com.example.sale.service.DataService;
import com.example.sale.service.SkService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.sale.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
@RestController
@Slf4j
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;

    @Autowired
    private SkService skService;


    @PostMapping("/login")
    @NoAuth
    public Result login(@RequestBody UserDTO userDTO,HttpServletRequest request){

        return Result.success(userService.login(userDTO,request));
    }

    @GetMapping("/info")
    @NoAuth
    public Result info(@RequestParam String token){
        return Result.success(userService.getUserByToken(token));
    }

    @RequestMapping("/logout")
    public Result logout(@RequestHeader("token")String token){
        userService.logout(token);
        return Result.success();
    }





    @RequestMapping("/getDataVd")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataVd(){
        return Result.success(userService.getDataVdForMul(GroupConstants.urls_wayv_vd,GroupConstants.names_wayv_2));
//        return Result.success(userService.getDataKm(GroupConstants.urls_aespa_km,GroupConstants.names_aespa_km));

    }

//    @RequestMapping("/getDataKMS")
//    @RequiresRoles(value = {"ADMIN"})
//    public Result getDataKMS(){
//        return Result.success(userService.getDataKMS(GroupConstants.urls_wayv_kms,GroupConstants.names_rv));
//    }

    @RequestMapping("/getDataKMSSale")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMSSale(){
        return Result.success(userService.getDataKMSForSale(GroupConstants.urls_wayv_kms,GroupConstants.names_wayv));
    }

    @RequestMapping("/getCountVd")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountVd(DataDTO dataDTO){
        return Result.success(dataService.getDataVd(dataDTO,GroupConstants.names_wayv_2,8));
//        List<String> names = Arrays.asList("单人拍照/拍手","团体拍照/签售");
//        return Result.success(dataService.getDataVd(dataDTO,names,5));
    }

//    @RequestMapping("/getCountKMS")
//    @RequiresRoles(value = {"ADMIN"})
//    public Result getCountKMS(DataDTO dataDTO){
//        return Result.success(dataService.getDataKMS(dataDTO,GroupConstants.names_sk,19));
//    }
//
    @RequestMapping("/getCountKMSSale")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMSSale(DataDTO dataDTO){
        return Result.success(dataService.getDataKMSSale(dataDTO,GroupConstants.names_wayv,8));
    }

    @RequestMapping("/searchVd")
    @RequiresRoles(value = {"ADMIN"})
    public Result searchVd(DataDTO dataDTO){

        return Result.success(dataService.searchVd(dataDTO));
//        List<String> names = Arrays.asList("单人拍照/拍手","团体拍照/签售");
//        return Result.success(dataService.getDataVd(dataDTO,names,5));
    }

    @RequestMapping("/searchKMS")
    @RequiresRoles(value = {"ADMIN"})
    public Result searchKMS(DataDTO dataDTO){
        return Result.success(dataService.searchKMS(dataDTO));
    }






    @RequestMapping("/getDataVd2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataVd2(){
        return Result.success(userService.getDataVdForMul(GroupConstants.urls_skg_vd,GroupConstants.names_skg_2));
//        return Result.success(skService.updateData());
    }

    @RequestMapping("/getCountVd2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountVd2(DataDTO dataDTO){
        return Result.success(dataService.getDataVd(dataDTO,GroupConstants.names_skg_2,1));
//        List<String> names = Arrays.asList("单人拍照/拍手","团体拍照/签售");
//        return Result.success(dataService.getDataVd(dataDTO,names,5));
    }

    @RequestMapping("/getDataKMS2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMS2(){
        return Result.success(userService.getDataKMSForMul(GroupConstants.urls_skg_kms,GroupConstants.names_skg_2));
    }


    @RequestMapping("/getCountKMS2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMS2(DataDTO dataDTO){
        return Result.success(dataService.getDataKMS(dataDTO,GroupConstants.names_skg_2,1));
    }

    @RequestMapping("/getDataKMSSale2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMSSale2(){
        return Result.success(userService.getDataKMSForSale(GroupConstants.urls_skg_kms,GroupConstants.names_skg));
    }

    @RequestMapping("/getCountKMSSale2")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMSSale2(DataDTO dataDTO){
        return Result.success(dataService.getDataKMSSale(dataDTO,GroupConstants.names_skg,1));
    }








    @RequestMapping("/getDataVd3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataVd3(){
        return Result.success(userService.getDataVdForMul(GroupConstants.urls_wendy_vd,GroupConstants.names_wendy_2));
//        return Result.success(skService.updateData());
    }

    @RequestMapping("/getCountVd3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountVd3(DataDTO dataDTO){
        return Result.success(dataService.getDataVd(dataDTO,GroupConstants.names_wendy_2,6));
//        List<String> names = Arrays.asList("单人拍照/拍手","团体拍照/签售");
//        return Result.success(dataService.getDataVd(dataDTO,names,5));
    }

    @RequestMapping("/getDataKMS3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMS3(){
        return Result.success(userService.getDataKMS(GroupConstants.urls_wendy_kms,GroupConstants.names_wendy));
    }


    @RequestMapping("/getCountKMS3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMS3(DataDTO dataDTO){
        return Result.success(dataService.getDataKMS(dataDTO,GroupConstants.names_wendy,6));
    }

    @RequestMapping("/getDataKMSSale3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getDataKMSSale3(){
        return Result.success(userService.getDataKMSForSale(GroupConstants.urls_wendy_kms,GroupConstants.names_wendy));
    }

    @RequestMapping("/getCountKMSSale3")
    @RequiresRoles(value = {"ADMIN"})
    public Result getCountKMSSale3(DataDTO dataDTO){
        return Result.success(dataService.getDataKMSSale(dataDTO,GroupConstants.names_wendy,6));
    }
}
