package com.example.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.SkDTO;
import com.example.sale.model.Person;
import com.example.sale.vo.SkVO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SkDao;
import com.example.sale.entity.SkEntity;
import com.example.sale.service.SkService;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@Service("skService")
public class SkServiceImpl extends ServiceImpl<SkDao, SkEntity> implements SkService, ResultConstants {

    @Autowired
    private SkDao skDao;

    @Override
    public List<SkVO> getData(Integer target) {
        updateData();
        List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(target!=null,SkEntity::getId,target));
        List<SkVO> res = new ArrayList<>();
        for (SkEntity entity:list){
            SkVO skVO = new SkVO();
            BeanUtils.copyProperties(entity,skVO);
            Integer stockTotal = entity.getStockTotal();
            Integer stock = entity.getStock();
            Integer sale = stockTotal-stock;
            skVO.setSale(sale);
            Integer a = sale/entity.getBig();
            Integer b = sale/entity.getSmall();
            skVO.setCut(a+"~"+b) ;
            res.add(skVO);
        }
        return res;
    }

    @Override
    public List<SkVO> getData1(Integer role) {
        updateData();
        List<Integer> ids = new ArrayList<>();
        List<SkVO> res = new ArrayList<>();
        switch (role){
            case 4:
                ids = Arrays.asList(36,37);
                break;
            default:
                ids = Arrays.asList(38,39);
        }
        List<SkEntity> list = skDao.selectBatchIds(ids);
        return generateVO(list);
    }

    @Override
    @Transactional
    public void edit(SkDTO skDTO) {
        SkEntity entity = skDao.selectById(skDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setStock(skDTO.getStock());
        skDao.updateById(entity);
    }

    private List<SkVO> generateVO(List<SkEntity> list){
        List<SkVO> res = new ArrayList<>();
        for(SkEntity entity:list){
            SkVO skVO = new SkVO();
            BeanUtils.copyProperties(entity,skVO);
            Integer stockTotal = entity.getStockTotal();
            Integer stock = entity.getStock();
            Integer sale = stockTotal-stock;
            skVO.setSale(sale);
            Integer a = sale/entity.getBig();
            Integer b = sale/entity.getSmall();
            skVO.setCut(a+"~"+b);
            res.add(skVO);
        }
        return res;
    }

    public void updateData(){
        String url = "https://api.whosfan.com.cn/api.php";
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(url);
        postMethod.addRequestHeader("Referer", "https://whosfan.com.cn/");
        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/116.0.0.0 Safari/537.36");
        // 添加请求头
        // 存储响应字符串并转化成json对象
        String res_str = "";
        JSONObject res_obj = null;
        try {
            int code = httpClient.executeMethod(postMethod);
            if (code == 200){
                List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<>());
                // 表为空
                if (list.size() == 0)
                    throw new BusinessException("此表无数据");
                // 获取开始的id
                Integer count = 0;
                // 处理响应结果
                res_str = postMethod.getResponseBodyAsString();
                res_obj = JSON.parseObject(res_str);
                Map<String,Object> data = (Map<String, Object>) res_obj.get("data");
                // 修改总销量
                List<Map<String,Object>> dataList = (List<Map<String, Object>>) data.get("data_list");
                List<Map<String,Object>> goods = (List<Map<String, Object>>) dataList.get(0).get("goods");
                for (int i=0;i<2;i++){
                    Map<String, Object> item = goods.get(i);
                    SkEntity entity1 = list.get(i);
                    SkEntity entity2 = list.get(i+2);
                    Integer stock = Integer.parseInt(item.get("inventory").toString());
                    entity1.setStock(stock);
                    entity2.setStock(stock);
                    skDao.updateById(entity1);
                    skDao.updateById(entity2);
                }

//                SkEntity entity = list.get(count++);
//                entity.setStock(itemStock);
//                skDao.updateById(entity);
//                // 修改单人销量
//                List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuInfos");
//                for (int i=0;i<5;i++){
//                    Map<String, Object> map = skuInfos.get(i);
//                    Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
//                    Integer stock = Integer.parseInt(skuInfo.get("stock").toString());
//                    entity = list.get(count++);
//                    entity.setStock(stock);
//                    skDao.updateById(entity);
//                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateData1(){
        String url = "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226549437648%22%7D&wdtoken=bc2502a6&_=1692160968431";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Referer", "https://shop1723959802.v.weidian.com/");
        getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        // 添加请求头
        // 存储响应字符串并转化成json对象
        String res_str = "";
        JSONObject res_obj = null;
        try {
            int code = httpClient.executeMethod(getMethod);
            if (code == 200){
                List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<>());
                // 表为空
                if (list.size() == 0)
                    throw new BusinessException("此表无数据");
                // 获取开始的id
                Integer count = 0;
                // 处理响应结果
                res_str = getMethod.getResponseBodyAsString();
                res_obj = JSON.parseObject(res_str);
                Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                // 修改总销量
                Integer itemStock = Integer.parseInt(result.get("itemStock").toString());
                SkEntity entity = list.get(count++);
                entity.setStock(itemStock);
                skDao.updateById(entity);
                // 修改单人销量
                List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuInfos");
                for (int i=0;i<5;i++){
                    Map<String, Object> map = skuInfos.get(i);
                    Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
                    Integer stock = Integer.parseInt(skuInfo.get("stock").toString());
                    entity = list.get(count++);
                    entity.setStock(stock);
                    skDao.updateById(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateData2(){
        String url = "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226548415867%22%7D&wdtoken=bc2502a6&_=1692158476549";
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        getMethod.addRequestHeader("Referer", "https://shop1723959802.v.weidian.com/");
        getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        // 添加请求头
        // 存储响应字符串并转化成json对象
        String res_str = "";
        JSONObject res_obj = null;
        try {
            int code = httpClient.executeMethod(getMethod);
            if (code == 200){
                List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<SkEntity>());
                // 表为空
                if (list.size() == 0)
                    throw new BusinessException("此表无数据");
                // 获取开始的id
                Integer count = 6;
                // 处理响应结果
                res_str = getMethod.getResponseBodyAsString();
                res_obj = JSON.parseObject(res_str);
                Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                // 修改总销量
                Integer itemStock = Integer.parseInt(result.get("itemStock").toString());
                SkEntity entity = list.get(count++);
                entity.setStock(itemStock);
                skDao.updateById(entity);
                // 修改单人销量
                List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuInfos");
                for (int i=0;i<15;){
                    Integer sum = 0;
                    for(int j=0;j<3;j++){
                        Map<String, Object> map = skuInfos.get(i++);
                        Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
                        Integer stock = Integer.parseInt(skuInfo.get("stock").toString());
                        sum += stock;
                    }
                    entity = list.get(count++);
                    entity.setStock(sum);
                    skDao.updateById(entity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateData3(){
        List<String> urls = Arrays.asList("https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226551247932%22%7D&wdtoken=781d3a55&_=1692190114280"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226550403305%22%7D&wdtoken=781d3a55&_=1692190281399"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226551313896%22%7D&wdtoken=781d3a55&_=1692190365013");
        List<Integer> ids = Arrays.asList(31,32,33);
        for (int i=0;i<3;i++) {
            Integer id = ids.get(i);
            SkEntity entity = skDao.selectById(id);
            String url = urls.get(i);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "https://shop1723959802.v.weidian.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
            // 存储响应字符串并转化成json对象
            String res_str = "";
            JSONObject res_obj = null;
            try {
                int code = httpClient.executeMethod(getMethod);
                if (code == 200){
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                    entity.setStock(Integer.parseInt(result.get("itemStock").toString()));
                    skDao.updateById(entity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}