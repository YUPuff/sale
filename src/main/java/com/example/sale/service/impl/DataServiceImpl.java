package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.dao.DataKmsDao;
import com.example.sale.dto.DataDTO;
import com.example.sale.entity.DataKmsEntity;
import com.example.sale.vo.DataVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.DataDao;
import com.example.sale.entity.DataEntity;
import com.example.sale.service.DataService;

import java.text.SimpleDateFormat;
import java.util.*;


@Service("dataService")
public class DataServiceImpl extends ServiceImpl<DataDao, DataEntity> implements DataService {

    @Autowired
    private DataDao dataDao;

    @Autowired
    private DataKmsDao dataKmsDao;

    @Override
    public List<DataVO> getDataVd(DataDTO dataDTO) {
        List<String> names = Arrays.asList("钟辰乐","罗渽民","黄仁俊","李马克","朴志晟","李帝努","李楷灿");
        List<DataVO> res = new ArrayList<>();
        // 获取用户输入的时间段
        String user_start = dataDTO.getStart();
        String user_end = dataDTO.getEnd();
        // 获取今天的时间段
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String today_start = today + " 00:00:00";
        String today_end = today + " 23:59:59";

        boolean flag1 = StringUtils.isNotBlank(user_start);
        boolean flag2 = StringUtils.isNotBlank(user_end);
        String start =  flag1 || flag2 ? user_start : today_start;
        String end =  flag1 || flag2 ? user_end : today_end;
        for (String name : names) {

            List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
                    .eq("name",name)
                    .ge(StringUtils.isNotBlank(start),"time",start)
                    .le(StringUtils.isNotBlank(end),"time",end)
                    .select("distinct stock"));
            DataVO dataVO = new DataVO(name,data);
            generateChange(dataVO);
            generateHistoryVd(dataVO,calendar,name);
            res.add(dataVO);
        }
        return res;
    }

    @Override
    public List<DataVO> getDataKMS(DataDTO dataDTO) {
        List<String> names = Arrays.asList("钟辰乐","罗渽民","黄仁俊","李马克","朴志晟","李帝努","李楷灿");
        List<DataVO> res = new ArrayList<>();
        // 获取用户输入的时间段
        String user_start = dataDTO.getStart();
        String user_end = dataDTO.getEnd();
        // 获取今天的时间段
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String today_start = today + " 00:00:00";
        String today_end = today + " 23:59:59";

        boolean flag1 = StringUtils.isNotBlank(user_start);
        boolean flag2 = StringUtils.isNotBlank(user_end);
        String start =  flag1 || flag2 ? user_start : today_start;
        String end =  flag1 || flag2 ? user_end : today_end;
        for (String name : names) {

            List<Object> data = dataKmsDao.selectObjs(new QueryWrapper<DataKmsEntity>()
                    .eq("name",name)
                    .ge(StringUtils.isNotBlank(start),"time",start)
                    .le(StringUtils.isNotBlank(end),"time",end)
                    .select("distinct stock"));
            DataVO dataVO = new DataVO(name,data);
            generateChange(dataVO);
            generateHistoryKMS(dataVO,calendar,name);
            res.add(dataVO);
        }
        return res;
    }

    private void generateHistoryVd(DataVO dataVO,Calendar calendar,String name){
        List<Map<String,Object>> list = new ArrayList<>();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, -(today-9));
        for (int i=9;i<today;i++){
            Map<String,Object> map = new HashMap<>();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String start = day + " 00:00:00";
            String end = day + " 23:59:59";
            List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("stock"));
            map.put("date","8月"+i+"号");
            map.put("起始",data.size()>1 ? data.get(0) : null);
            map.put("末尾",data.size()>1 ? data.get(data.size()-1) : null);
            list.add(map);
            calendar.add(Calendar.DATE,1);
        }
        dataVO.setHistory(list);
    }

    private void generateHistoryKMS(DataVO dataVO,Calendar calendar,String name){
        List<Map<String,Object>> list = new ArrayList<>();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, -(today-9));
        for (int i=9;i<today;i++){
            Map<String,Object> map = new HashMap<>();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String start = day + " 00:00:00";
            String end = day + " 23:59:59";
            List<Object> data = dataKmsDao.selectObjs(new QueryWrapper<DataKmsEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("stock"));
            map.put("date","8月"+i+"号");
            map.put("起始",data.size()>1 ? data.get(0) : null);
            map.put("末尾",data.size()>1 ? data.get(data.size()-1) : null);
            list.add(map);
            calendar.add(Calendar.DATE,1);
        }
        dataVO.setHistory(list);
    }

    private void generateChange(DataVO dataVO){
        List<Object> stocks = dataVO.getStocks();
        Map<String,Integer> map = new HashMap<>();
        Integer pre = (stocks.size() == 0 ? null : Integer.parseInt(stocks.get(0).toString()));
        for (int i=1;i<stocks.size();i++){
            Integer curr = Integer.parseInt(stocks.get(i).toString());
            Integer interval = pre-curr;
            pre = curr;
            String key = "超过100";
            if (interval<=0)
                continue;
            else if (interval<21){
                key = "1~20";
            }else if (interval<51){
                key = "21~50";
            }else if (interval<71){
                key = "51~70";
            }else if (interval<101){
                key = "71~100";
            }
            map.put(key,map.getOrDefault(key,0)+1);
        }
        dataVO.setChange(map);
    }
}