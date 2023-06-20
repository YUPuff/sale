package com.example.sale.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.sale.dto.UserDTO;
import com.example.sale.entity.UserEntity;
import com.example.sale.vo.UserVO;

import java.util.Map;

/**
 * @author yilin
 * @date 2023-06-19 15:50:56
 */
public interface UserService extends IService<UserEntity> {

    UserVO login(UserDTO userDTO);

    UserVO getUserByToken(String token);

    void logout(String token);

}

