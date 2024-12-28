package com.example.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.RedisConstants;
import com.example.sale.dao.LogDao;
import com.example.sale.dto.DataDTO;
import com.example.sale.dto.IpDTO;
import com.example.sale.dto.UserDTO;
import com.example.sale.entity.LogEntity;
import com.example.sale.entity.SkEntity;
import com.example.sale.model.Person;
import com.example.sale.model.PersonSale;
import com.example.sale.utils.IpUtils;
import com.example.sale.utils.JWTUtils;
import com.example.sale.vo.DataVO;
import com.example.sale.vo.UserVO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.UserDao;
import com.example.sale.entity.UserEntity;
import com.example.sale.service.UserService;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.apache.shiro.SecurityUtils.getSubject;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, UserEntity> implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public UserVO login(UserDTO userDTO, HttpServletRequest request) {
        String name = userDTO.getUsername();
        UserEntity user = userDao.selectOne(new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getName,name));
        if (user == null)
            throw new BusinessException("用户不存在");
        if (!userDTO.getPassword().equals(user.getPassword()))
            throw new BusinessException("密码错误");

//        // 获取IP地址
//        String ip = IpUtils.getIpAddr(request);
//        IpDTO ipDTO = null;
//        try {
//            ipDTO = IpUtils.getRegion(ip);
//        } catch (Exception e) {
//            throw new BusinessException("获取ip归属地信息失败！");
//        }
////        if ("jj1323".equals(name))
////            throw new BusinessException("检测到您多IP登陆，账号已被锁定，请联系管理员！");
//        logDao.insert(new LogEntity(name,ip,ipDTO.generateRegion()));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        userVO.setName(name);
        generateToken(userVO);
        return userVO;
    }

    @Override
    public UserVO getUserByToken(String token) {
        String json = redisTemplate.opsForValue().get(RedisConstants.TOKEN+token);
        return JSON.parseObject(json,UserVO.class);
    }

    @Override
    public void logout(String token) {
        getSubject().logout();
        redisTemplate.delete(RedisConstants.TOKEN+token);
    }

    @Override
    public List<Person> getDataVd(List<String> urls,List<String> names) {
        List<Person> res = new ArrayList<>();
        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "https://shop1382036085.v.weidian.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            getMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                    res.add(new Person(name,result.get("itemStock").toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<Person> getDataKm(List<String> urls, List<String> names) {
        List<Person> res = new ArrayList<>();
        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "https://kmonstar.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    Map<String,Object> data = (Map<String, Object>) res_obj.get("data");
                    Map<String,Object> product = (Map<String, Object>) data.get("product");
                    res.add(new Person(name,product.get("total_quantity").toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<Person> getDataKm2(List<String> urls, List<String> names) {
        List<Person> res = new ArrayList<>();
        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "https://www.kmonstar.com.tw/products/%E6%87%89%E5%8B%9F-240505-moon-byul-1st-full-album-starlit-of-muse-%E5%B0%88%E8%BC%AF%E7%99%BC%E8%A1%8C%E7%B4%80%E5%BF%B5%E7%B2%89%E7%B5%B2%E8%A6%8B%E9%9D%A2%E6%9C%83-in-kaohsiung");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    List<Map<String,Object>> data = (List<Map<String, Object>>) res_obj.get("variants");
                    Map<String,Object> product = data.get(0);
                    Integer stock = -Integer.parseInt(product.get("inventory_quantity").toString());
                    res.add(new Person(name,stock.toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<Person> getDataSR(String url,List<String> ids) throws UnsupportedEncodingException {
        List<Person> res = new ArrayList<>();
        Map<String,Integer> count = new HashMap<>();
        int j = 0;
        for (int i=0;i<ids.size();i++){
            String id = ids.get(i);
//            String name = null;
            HttpClient httpClient = new HttpClient();
            PostMethod postMethod = new PostMethod(url);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("activityId",22);
            jsonObject.put("id",id);
            String  toJson = jsonObject.toString();
            RequestEntity se = new StringRequestEntity(toJson ,"application/json" ,"UTF-8");

            postMethod.setRequestEntity(se);
            postMethod.addRequestHeader("Referer", "https://h5.xinghejimei.com/");
            postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            // 添加请求头
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(postMethod);
                if (code == 200){
                    res_str = postMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    System.out.println(res_obj);
                    System.out.println(res_str);
                    Map<String,Object> result = (Map<String, Object>) res_obj.get("data");
                    List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuList");
                    for(Map<String,Object> item:skuInfos){
                        String name = item.get("skuName").toString();
                        Integer stock = Integer.parseInt(item.get("stock").toString());
                        Integer curr_stock = count.getOrDefault(name,0);
                        count.put(name,stock+curr_stock);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(String name:count.keySet()){
            res.add(new Person(name,count.get(name).toString()));
        }
        return res;
    }

    @Override
    public List<Person> getDataVdForMul(List<String> urls, List<String> names) {
        List<Person> res = new ArrayList<>();
        int j = 0;
        for (int i=0;i<urls.size();i++){
            String url = urls.get(i);
//            String name = null;
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
//            getMethod.addRequestHeader("Connection", "close");
            getMethod.addRequestHeader("Referer", "https://shop1382036085.v.weidian.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36");
            // 添加请求头
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                    List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuInfos");
                    for(int x=0;x<skuInfos.size();x+=2){
                        Map<String, Object> map = skuInfos.get(x);
//                        Map<String, Object> map_200 = skuInfos.get(x+1);
                        String name = names.get(j++);
                        Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
//                        Map<String,Object> skuInfo_200 = (Map<String, Object>) map_200.get("skuInfo");
//                        String name = skuInfo.get("title").toString().split(";")[0];
                        res.add(new Person(name,skuInfo.get("stock").toString()));
                    }
//                    for (Map<String, Object> map : skuInfos){
//                        String name = names.get(j++);
//                        Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
////                        String name = skuInfo.get("title").toString().split(";")[0];
//                        res.add(new Person(name,skuInfo.get("stock").toString()));
//
//                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }


    @Override
    public List<Person> getDataKMS(List<String> urls,List<String> names) {
        List<Person> res = new ArrayList<>();

        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "http://page.kmstation.net/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
//            getMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    res.add(new Person(name,res_obj.get("totalStocks").toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<Person> getDataKMSForMul(List<String> urls, List<String> names) {
        List<Person> res = new ArrayList<>();
        int j = 0;
        for (int i=0;i<urls.size();i++){
            String url = urls.get(i);
//            String name = null;
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.addRequestHeader("Referer", "http://page.kmstation.net/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/131.0.0.0 Safari/537.36");
            getMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // 添加请求头
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    List<Map<String,Object>> skuList = (List<Map<String, Object>>) res_obj.get("skuList");
                    if (skuList.isEmpty()){
                        String name = names.get(j++);
//                        String name = skuInfo.get("title").toString().split(";")[0];
                        res.add(new Person(name,"0"));
                    }
                    for (Map<String, Object> map : skuList){
                        String name = names.get(j);
                        j++;
//                        String name = skuInfo.get("title").toString().split(";")[0];
                        res.add(new Person(name,map.get("stocks").toString()));

                        if(j%7 == 0) break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<PersonSale> getDataKMSForSale(List<String> urls, List<String> names) {
        List<PersonSale> res = new ArrayList<>();

        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "http://page.kmstation.net/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            getMethod.addRequestHeader("Content-Type", "application/json;charset=UTF-8");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    res.add(new PersonSale(name,res_obj.get("monthSales").toString(),res_obj.get("soldNum").toString()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    @Override
    public List<Person> getDataKMSList(List<String> names) {
        List<Person> res = new ArrayList<>();
        int j = 0;
        for (int i=1;i<3;i++){
            String url = "https://kms.kmstation.net/p/user/collection/page?current="+i+"&size=10";
//            String name = null;
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.addRequestHeader("Referer", "http://page.kmstation.net/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            getMethod.addRequestHeader("Authorization", "bearer 17737ede-4a1f-4f70-9659-50b74b39598d");
            // 添加请求头
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    List<Map<String,Object>> records = (List<Map<String, Object>>) res_obj.get("records");
//                    if (records.isEmpty()){
//                        String name = names.get(j++);
////                        String name = skuInfo.get("title").toString().split(";")[0];
//                        res.add(new Person(name,"0"));
//                    }
                    for (Map<String, Object> map : records){
                        String name = names.get(j++);
//                        String name = skuInfo.get("title").toString().split(";")[0];
                        res.add(new Person(name,map.get("soldNum").toString()));
//                        if(j%7 == 0) break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    private void generateToken(UserVO userVO){
        String token = JWTUtils.sign(userVO.getId());
        redisTemplate.opsForValue().set(RedisConstants.TOKEN+token, JSON.toJSONString(userVO),7, TimeUnit.DAYS);
        userVO.setToken(token);
    }
}