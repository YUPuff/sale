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
import com.example.sale.model.Person;
import com.example.sale.utils.IpUtils;
import com.example.sale.utils.JWTUtils;
import com.example.sale.vo.DataVO;
import com.example.sale.vo.UserVO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.UserDao;
import com.example.sale.entity.UserEntity;
import com.example.sale.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

        // 获取IP地址
        String ip = IpUtils.getIpAddr(request);
        IpDTO ipDTO = null;
        try {
            ipDTO = IpUtils.getRegion(ip);
        } catch (Exception e) {
            throw new BusinessException("获取ip归属地信息失败！");
        }
        if (("Chongqing".equals(ipDTO.getRegionName()) && name.startsWith("nd")) && !name.equals("nd-ps1")||
                ("Henan".equals(ipDTO.getRegionName()) && name.equals("nd-qp")))
            throw new BusinessException("无法访问，请联系管理员！");
        logDao.insert(new LogEntity(name,ip,ipDTO.generateRegion()));
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
    public List<Person> getDataVd() {
        List<Person> res = new ArrayList<>();
        List<String> urls = Arrays.asList("https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226537108792%22%7D&wdtoken=29fefbac&_=1691586031586"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226537205772%22%7D&wdtoken=29fefbac&_=1691586998110"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226536135851%22%7D&wdtoken=29fefbac&_=1691586113415"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226537101032%22%7D&wdtoken=29fefbac&_=1691586151613"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226536133889%22%7D&wdtoken=29fefbac&_=1691586365092"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226536094189%22%7D&wdtoken=29fefbac&_=1691586390049"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226537110684%22%7D&wdtoken=29fefbac&_=1691586491887");
        List<String> names = Arrays.asList("钟辰乐","罗渽民","黄仁俊","李马克","朴志晟","李帝努","李楷灿");
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
    public List<Person> getDataKMS() {
        List<Person> res = new ArrayList<>();
        List<String> urls = Arrays.asList("https://kms.kmstation.net/prod/prodInfo?prodId=2781"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2782"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2778"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2783"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2784"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2779"
                ,"https://kms.kmstation.net/prod/prodInfo?prodId=2780");
        List<String> names = Arrays.asList("钟辰乐","罗渽民","黄仁俊","李马克","朴志晟","李帝努","李楷灿");
        for (int i=0;i<names.size();i++) {
            String name = names.get(i);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "http://page.kmstation.net/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
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

    private void generateToken(UserVO userVO){
        String token = JWTUtils.sign(userVO.getId());
        redisTemplate.opsForValue().set(RedisConstants.TOKEN+token, JSON.toJSONString(userVO),7, TimeUnit.DAYS);
        userVO.setToken(token);
    }
}