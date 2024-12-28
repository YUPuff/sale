package com.example.sale.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.GroupConstants;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dto.SkDTO;
import com.example.sale.entity.SvEntity;
import com.example.sale.model.Person;
import com.example.sale.service.DataService;
import com.example.sale.service.UserService;
import com.example.sale.vo.DataVO;
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
import java.util.*;


@Service("skService")
public class SkServiceImpl extends ServiceImpl<SkDao, SkEntity> implements SkService, ResultConstants {

    @Autowired
    private SkDao skDao;

    @Autowired
    private UserService userService;

    @Autowired
    private DataService dataService;

    @Override
    public List<SkVO> getData(Integer target, Integer role) {
//        updateData1();
        List<SkVO> res = new ArrayList<>();
        List<SkEntity> list = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        switch (role){
            case 9:
                ids = Arrays.asList(83, 84,88,89);
                list = skDao.selectBatchIds(ids);
                break;
            case 5:
                ids = Arrays.asList(83, 87);
                list = skDao.selectBatchIds(ids);
                break;
            case 6:
                ids = Arrays.asList(83, 90);
                list = skDao.selectBatchIds(ids);
                break;
            case 7:
                ids = Arrays.asList(83, 88);
                list = skDao.selectBatchIds(ids);
                break;
            case 8:
                ids = Arrays.asList(87, 94);
                list = skDao.selectBatchIds(ids);
                break;
            case 12:
                ids = Arrays.asList(88, 95);
                list = skDao.selectBatchIds(ids);
                break;
            case 13:
                ids = Arrays.asList(89, 96);
                list = skDao.selectBatchIds(ids);
                break;
            default:
                list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(target!=null,SkEntity::getId,target));
        }

//        List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(target!=null,SkEntity::getId,target));
//        if(target == null) {
//            List<Integer> ids = Arrays.asList(79, 80);
//            list = skDao.selectBatchIds(ids);
//        }else{
//            list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(SkEntity::getId,target));
//        }

        for (SkEntity entity:list){
            SkVO skVO = new SkVO();
            String name = entity.getName();
            DataVO data_vd = dataService.getChangeVd(name);
            DataVO data_kms = dataService.getChangeKMS(name);
            BeanUtils.copyProperties(entity,skVO);
            Integer stockTotal = entity.getStockTotal();
            Integer stock = entity.getStock();
            Integer sale = stockTotal-stock;
            skVO.setSale(sale);
            Integer a = sale/entity.getBig();
            Integer b = sale/entity.getSmall();
            skVO.setCut(a+"~"+b);
            skVO.setChange_vd(data_vd.getChange());
            skVO.setChange_kms(data_kms.getChange());
            res.add(skVO);
        }
        return res;
//        return generateVOForSum(list);
    }

    @Override
    public List<SkVO> getData1(Integer role) {
        updateData1();
        List<Integer> ids = new ArrayList<>();
        List<SkVO> res = new ArrayList<>();
//        switch (role){
//            case 14:
//                ids = Arrays.asList(40,41);
//                break;
//            default:
//                ids = Arrays.asList(40,41);
//        }
        List<SkEntity> list = skDao.selectBatchIds(ids);
//        return generateVO(list);
        return generateVOForSum(list);
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

    private List<SkVO> generateVOForSum(List<SkEntity> list){
        List<SkVO> res = new ArrayList<>();
        Integer sum = 0;
        Integer small = 1;
        Integer big = 1;
        for(int i=0;i<list.size();i++){
            SkEntity entity = list.get(i);
            SkVO skVO = new SkVO();
            BeanUtils.copyProperties(entity,skVO);
            Integer stockTotal = entity.getStockTotal();
            Integer stock = entity.getStock();
            Integer sale = stockTotal-stock;
            if (i>1)
                sale *= 9;
            skVO.setSale(sale);
            sum += sale;
            big = entity.getBig();
            small = entity.getSmall();
            res.add(skVO);
        }
        res.add(new SkVO("总计",sum,sum/big+"~"+sum/small));
        return res;
    }
    @Override
    public List<Person> updateData(){
        List<Person> res = new ArrayList<>();
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
                    if (i == 0)
                        res.add(new Person("单人拍照/拍手",stock.toString()));
                    else
                        res.add(new Person("团体拍照/签售",stock.toString()));
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
        return res;
    }

    private void updateData1(){
        List<String> urls = Arrays.asList(
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227296378501%22%7D&wdtoken=7d0791d3&_=1730602080480",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227296103973%22%7D&wdtoken=67d5e611&_=1730614387012"
        );
        Map<String,Integer> res = new HashMap<>();
        for (String url:urls){
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            getMethod.addRequestHeader("Referer", "https://shop1649976533.v.weidian.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/130.0.0.0 Safari/537.36");
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
                    for (Map<String, Object> map : skuInfos){
                        Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
//                        String name = skuInfo.get("title").toString().split(";")[0];
                        String name = skuInfo.get("title").toString();
                        Integer stock = Integer.parseInt(skuInfo.get("stock").toString());
                        res.put(name,res.getOrDefault(name,0)+stock);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String name:res.keySet()){
            SkEntity entity = skDao.selectOne(new LambdaQueryWrapper<SkEntity>().eq(SkEntity::getName, name));
            if (entity == null)
                throw new BusinessException("搜索不存在！");
            entity.setStock(res.get(name));
            skDao.updateById(entity);
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
        List<String> urls = Arrays.asList(
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227235061369%22%7D&wdtoken=f1831195&_=1713862022599",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227235051409%22%7D&wdtoken=f1831195&_=1713862064047",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227236161516%22%7D&wdtoken=b6d06d5d&_=1714029464961",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%227235303653%22%7D&wdtoken=b6d06d5d&_=1714029701675"
        );
        for (int i=40;i<=43;i++) {
            SkEntity entity = skDao.selectById(i);
            String url = urls.get(i-40);
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            // 添加请求头
            getMethod.addRequestHeader("Referer", "https://shop2179628.v.weidian.com/");
            getMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
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

    public void updateData4(){
        List<Person> vd = userService.getDataVdForMul(GroupConstants.urls_dz_vd, GroupConstants.names_dz);
        List<Person> kms = userService.getDataKMSForMul(GroupConstants.urls_dz_kms, GroupConstants.names_dz);
        for(int i=0;i<vd.size();i++){
            Person p1 = vd.get(i);
            Person p2 = kms.get(i);
            Integer stock = Integer.parseInt(p1.getStock())+Integer.parseInt(p2.getStock());
            SkEntity entity = skDao.selectOne(new LambdaQueryWrapper<SkEntity>().eq(SkEntity::getName, p1.getName()));
            if (entity == null)
                throw new BusinessException("搜索不存在！");
            entity.setStock(stock);
            skDao.updateById(entity);
        }
    }

    public void updateDataForSK(List<Person> persons){
        for(int i=0;i<persons.size();i++){
            Person p1 = persons.get(i);
            Integer stock = Integer.parseInt(p1.getStock());
            SkEntity entity = skDao.selectOne(new LambdaQueryWrapper<SkEntity>().eq(SkEntity::getName, p1.getName()));
            if (entity == null)
                throw new BusinessException("搜索不存在！");
            entity.setStock(stock);
            skDao.updateById(entity);
        }
    }
}