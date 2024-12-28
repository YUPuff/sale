package com.example.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.CommonDTO;
import com.example.sale.model.Person;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.YzyVO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.YzyDao;
import com.example.sale.entity.YzyEntity;
import com.example.sale.service.YzyService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service("yzyService")
public class YzyServiceImpl extends ServiceImpl<YzyDao, YzyEntity> implements YzyService, ResultConstants {

    @Autowired
    private YzyDao yzyDao;

    @Override
    public List<YzyVO> getData(Integer target) {
        List<YzyEntity> list = yzyDao.selectList(new LambdaQueryWrapper<YzyEntity>().eq(target!=null,YzyEntity::getId,target));
        List<YzyVO> res = new ArrayList<>();
        for (YzyEntity entity:list){
            res.add(generateVO(entity));
        }
        return res;
    }

    /**
     * @param role
     * @return
     */
    @Override
    public List<YzyVO> getData1(Integer role) {
        List<YzyVO> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        if (role == 11){
//            list = Arrays.asList(183,182,180,184,181);
        }else if (role == 9){
            list = Arrays.asList(172,174,175,177);
        }else if (role == 12){
//            list = Arrays.asList(174,175);
        }


        for (Integer id : list) {
            YzyEntity entity = yzyDao.selectById(id);
            res.add(generateVO(entity));
        }
        return res;
    }

    @Override
    public void edit(CommonDTO commonDTO) {
        YzyEntity entity = yzyDao.selectById(commonDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setSale(commonDTO.getSale());
        yzyDao.updateById(entity);
    }

    @Override
    public String getYao() {
        StringBuffer sb = new StringBuffer();
        String url = "https://api.whosfan.com.cn/api.php";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        // 添加请求头
        getMethod.addRequestHeader("Referer", "https://whosfan.com.cn/");
        getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        // 存储响应字符串并转化成json对象
        String res_str = "";
        JSONObject res_obj = null;
        try {
            int code = httpClient.executeMethod(getMethod);
            if (code == 200){
                res_str = getMethod.getResponseBodyAsString();
                res_obj = JSON.parseObject(res_str);
                Map<String,Object> data = (Map<String, Object>) res_obj.get("data");
                List <Map<String,Object>> dataList = (List<Map<String, Object>>) data.get("data_list");
                List <Map<String,Object>> goods = (List<Map<String, Object>>) dataList.get(0).get("goods");
                Map<String,Object> specifications = (Map<String, Object>) goods.get(0).get("specifications");
                List <Map<String,Object>> choose = (List<Map<String, Object>>) specifications.get("choose");
                List <Map<String,Object>> value = (List<Map<String, Object>>) choose.get(0).get("value");
                sb.append("中国地址：").append(value.get(0).get("inventory")).append("；");
                sb.append("韩国地址：").append(value.get(1).get("inventory"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private YzyVO generateVO(YzyEntity entity){
        YzyVO yzyVO = new YzyVO();
        BeanUtils.copyProperties(entity,yzyVO);
        Integer sale = yzyVO.getSale();
        if ((entity.getId() == 171) && UserThreadLocal.get().getRole() != 0)
            yzyVO.setSale(0);
        Integer a = sale/entity.getBig();
        Integer b = sale/entity.getSmall();
        yzyVO.setCut(a+"~"+b);
        return yzyVO;
    }

}