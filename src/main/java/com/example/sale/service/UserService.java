package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.DataDTO;
import com.example.sale.dto.UserDTO;
import com.example.sale.entity.UserEntity;
import com.example.sale.model.Person;
import com.example.sale.model.PersonSale;
import com.example.sale.vo.DataVO;
import com.example.sale.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
public interface UserService extends IService<UserEntity> {

    UserVO login(UserDTO userDTO, HttpServletRequest request);

    UserVO getUserByToken(String token);

    void logout(String token);

    List<Person> getDataVd(List<String> urls,List<String> names);

    List<Person> getDataKm(List<String> urls,List<String> names);
    List<Person> getDataKm2(List<String> urls,List<String> names);

    List<Person> getDataSR(String url,List<String> ids) throws UnsupportedEncodingException;

    List<Person> getDataVdForMul(List<String> urls,List<String> names);

    List<Person> getDataKMS(List<String> urls,List<String> names);

    List<Person> getDataKMSForMul(List<String> urls,List<String> names);

    List<PersonSale> getDataKMSForSale(List<String> urls, List<String> names);

    List<Person> getDataKMSList(List<String> names);

}

