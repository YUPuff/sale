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
import java.util.*;


@Service("skService")
public class SkServiceImpl extends ServiceImpl<SkDao, SkEntity> implements SkService, ResultConstants {

    @Autowired
    private SkDao skDao;

    @Override
    public List<SkVO> getData(Integer target) {
        updateData3();
        List<SkEntity> list = skDao.selectList(new LambdaQueryWrapper<SkEntity>().eq(target!=null,SkEntity::getId,target));
//        List<SkVO> res = new ArrayList<>();
//        for (SkEntity entity:list){
//            SkVO skVO = new SkVO();
//            BeanUtils.copyProperties(entity,skVO);
//            Integer stockTotal = entity.getStockTotal();
//            Integer stock = entity.getStock();
//            Integer sale = stockTotal-stock;
//            skVO.setSale(sale);
//            Integer a = sale/entity.getBig();
//            Integer b = sale/entity.getSmall();
//            skVO.setCut(a+"~"+b) ;
//            res.add(skVO);
//        }
//        return res;
        return generateVOForSum(list);
    }

    @Override
    public List<SkVO> getData1(Integer role) {
        updateData3();
        List<Integer> ids = new ArrayList<>();
        List<SkVO> res = new ArrayList<>();
        switch (role){
            case 14:
                ids = Arrays.asList(40,41);
                break;
            default:
                ids = Arrays.asList(40,41);
        }
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
            if (i>list.size()-3)
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
        List<String> urls = Arrays.asList("https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226691075153%22%7D&wdtoken=1254c38a&_=1696940132979"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226694804763%22%7D&wdtoken=81ad8773&_=1697120714238"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226694835207%22%7D&wdtoken=81ad8773&_=1697120491588"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226695757022%22%7D&wdtoken=81ad8773&_=1697120676753"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226695281871%22%7D&wdtoken=81ad8773&_=1697120786188"
                ,"https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226694868409%22%7D&wdtoken=81ad8773&_=1697120829413");
        Map<String,Integer> res = new HashMap<>();
        for (String url:urls){
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
                    res_str = getMethod.getResponseBodyAsString();
                    res_obj = JSON.parseObject(res_str);
                    Map<String,Object> result = (Map<String, Object>) res_obj.get("result");
                    List<Map<String,Object>> skuInfos = (List<Map<String, Object>>) result.get("skuInfos");
                    for (Map<String, Object> map : skuInfos){
                        Map<String,Object> skuInfo = (Map<String, Object>) map.get("skuInfo");
                        String name = skuInfo.get("title").toString().split(";")[0];
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
        List<String> urls = Arrays.asList("https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226735426933%22%7D&wdtoken=a6a00fec&_=1697788091671",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226736392902%22%7D&wdtoken=a6a00fec&_=1697788266227",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743725200%22%7D&wdtoken=947a95a1&_=1698296396085",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742745311%22%7D&wdtoken=bcc0bc6c&_=1698316037161",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742756739%22%7D&wdtoken=947a95a1&_=1698296950678",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742849763%22%7D&wdtoken=947a95a1&_=1698297015951",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742756683%22%7D&wdtoken=947a95a1&_=1698297172853",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743758374%22%7D&wdtoken=bcc0bc6c&_=1698315963113",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742780189%22%7D&wdtoken=947a95a1&_=1698297219140",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743775956%22%7D&wdtoken=bcc0bc6c&_=1698315854450",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226740810607%22%7D&wdtoken=947a95a1&_=1698297363455",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226741798358%22%7D&wdtoken=bcc0bc6c&_=1698316101033",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743810428%22%7D&wdtoken=947a95a1&_=1698297494348",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742848383%22%7D&wdtoken=bcc0bc6c&_=1698316432432",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743803724%22%7D&wdtoken=bcc0bc6c&_=1698316495752",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743712552%22%7D&wdtoken=bcc0bc6c&_=1698316551252",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742740513%22%7D&wdtoken=bcc0bc6c&_=1698316580344",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743777872%22%7D&wdtoken=bcc0bc6c&_=1698316665514",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743739952%22%7D&wdtoken=bcc0bc6c&_=1698316814192",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743734000%22%7D&wdtoken=bcc0bc6c&_=1698316885266",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226744067414%22%7D&wdtoken=bcc0bc6c&_=1698316934468",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743085543%22%7D&wdtoken=bcc0bc6c&_=1698316985070",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742992023%22%7D&wdtoken=bcc0bc6c&_=1698317006777",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742894963%22%7D&wdtoken=bcc0bc6c&_=1698317063234",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226742756533%22%7D&wdtoken=bcc0bc6c&_=1698317085906",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226743724628%22%7D&wdtoken=bcc0bc6c&_=1698317124594",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226740204738%22%7D&wdtoken=d2f5ed99&_=1698894494330",
                "https://thor.weidian.com/detail/getItemSkuInfo/1.0?param=%7B%22itemId%22%3A%226740218910%22%7D&wdtoken=d2f5ed99&_=1698902050822");
        for (int i=40;i<=67;i++) {
            SkEntity entity = skDao.selectById(i);
            String url = urls.get(i-40);
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