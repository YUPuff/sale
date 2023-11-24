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
        if (role == 9){
//            list = Arrays.asList(133,138,137);
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
        String res = "null";
        String url = "https://h5.youzan.com/wscgoods/tee-app/detail-v2.json?1005=undefined&activityId=&activityType=&alg=&alias=2737ekcfldnfwig&banner_id=~image_ad.1~0~GvVb8ChM&bizEnv=wsc&card_type=0&client=weapp&dc_ps=&from_uuid=en6QBIhcKg0Lieh1694777941294&fullPresaleSupportCart=true&img_ps=20%257C&isGoodsWeappNative=1&is_share=1&kdt_id=127635340&mpVersion=2.149.8&platform=weixin&scene=1005&share_from=weappCard&shopAutoEnter=1&subKdtId=0&umpAlias=&umpType=&withoutSkuDirectOrder=1&ump_alias=&ump_type=&oid=0&isDetailPrefetch=1%20HTTP/1.1";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        // 添加请求头
        getMethod.addRequestHeader("Referer", "https://servicewechat.com/wx360cd2418640589a/11/page-frame.html");
        getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/107.0.0.0 Safari/537.36 MicroMessenger/7.0.20.1781(0x6700143B) NetType/WIFI MiniProgramEnv/Windows WindowsWechat/WMPF XWEB/8391");
        // 存储响应字符串并转化成json对象
        String res_str = "";
        JSONObject res_obj = null;
        try {
            int code = httpClient.executeMethod(getMethod);
            if (code == 200){
                res_str = getMethod.getResponseBodyAsString();
                res_obj = JSON.parseObject(res_str);
                Map<String,Object> data = (Map<String, Object>) res_obj.get("data");
                Map<String,Object> spuStock = (Map<String, Object>) data.get("spuStock");
                res = spuStock.get("stockNum").toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    private YzyVO generateVO(YzyEntity entity){
        YzyVO yzyVO = new YzyVO();
        BeanUtils.copyProperties(entity,yzyVO);
        Integer sale = yzyVO.getSale();
//        if ((entity.getId() == 100 || entity.getId() == 102) && UserThreadLocal.get().getRole() != 0)
//            yzyVO.setSale(0);
        Integer a = sale/entity.getBig();
        Integer b = sale/entity.getSmall();
        yzyVO.setCut(a+"~"+b);
        return yzyVO;
    }

}