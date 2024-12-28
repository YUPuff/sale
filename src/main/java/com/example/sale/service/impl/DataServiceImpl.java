package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.dao.DataKmsDao;
import com.example.sale.dao.DataKmsSaleDao;
import com.example.sale.dto.DataDTO;
import com.example.sale.entity.DataKmsEntity;
import com.example.sale.entity.DataKmsSaleEntity;
import com.example.sale.vo.DataMulVO;
import com.example.sale.vo.DataVO;
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

    @Autowired
    private DataKmsSaleDao dataKmsSaleDao;

    @Override
    public List<DataVO> getDataVd(DataDTO dataDTO,List<String> names,Integer begin) {
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
            generateHistoryVd(dataVO,calendar,name,begin);
            res.add(dataVO);
        }
        return res;
    }

    @Override
    public List<DataVO> getDataKMS(DataDTO dataDTO,List<String> names,Integer begin) {
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
            generateHistoryKMS(dataVO,calendar,name,begin);
            res.add(dataVO);
        }
        return res;
    }

    @Override
    public List<DataMulVO> getDataKMSSale(DataDTO dataDTO, List<String> names, Integer begin) {
        List<DataMulVO> res = new ArrayList<>();
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

            List<Object> stocksMS = dataKmsSaleDao.selectObjs(new QueryWrapper<DataKmsSaleEntity>()
                    .eq("name",name)
                    .ge(StringUtils.isNotBlank(start),"time",start)
                    .le(StringUtils.isNotBlank(end),"time",end)
                    .select("distinct month_sale"));
            List<Object> stocksSN = dataKmsSaleDao.selectObjs(new QueryWrapper<DataKmsSaleEntity>()
                    .eq("name",name)
                    .ge(StringUtils.isNotBlank(start),"time",start)
                    .le(StringUtils.isNotBlank(end),"time",end)
                    .select("distinct sold_num"));
            DataMulVO dataVO = new DataMulVO(name,stocksMS,stocksSN);
            generateChangeForMul(dataVO);
            generateHistoryKMSForMul(dataVO,calendar,name,begin);
            res.add(dataVO);
        }
        return res;
    }

    @Override
    public List<DataEntity> searchVd(DataDTO dataDTO) {
        String small = dataDTO.getSmall();
        String big = dataDTO.getBig();
        if (small == null || big == null || !(StringUtils.isNumeric(small) && StringUtils.isNumeric(big)))
            throw new BusinessException("输入数字格式不正确！");
        List<DataEntity> res = dataDao.selectList(new LambdaQueryWrapper<DataEntity>()
                .eq(StringUtils.isNotBlank(dataDTO.getName()), DataEntity::getName, dataDTO.getName())
                .ge(DataEntity::getStock, Integer.parseInt(small))
                .le(DataEntity::getStock, Integer.parseInt(big)));
        return res;
    }

    @Override
    public List<DataKmsEntity> searchKMS(DataDTO dataDTO) {
        String small = dataDTO.getSmall();
        String big = dataDTO.getBig();
        if (small == null || big == null || !(StringUtils.isNumeric(small) && StringUtils.isNumeric(big)))
            throw new BusinessException("输入数字格式不正确！");
        List<DataKmsEntity> res = dataKmsDao.selectList(new LambdaQueryWrapper<DataKmsEntity>()
                .eq(StringUtils.isNotBlank(dataDTO.getName()), DataKmsEntity::getName, dataDTO.getName())
                .ge(DataKmsEntity::getStock, Integer.parseInt(small))
                .le(DataKmsEntity::getStock, Integer.parseInt(big)));
        return res;
    }

    private void generateHistoryVd(DataVO dataVO,Calendar calendar,String name,Integer begin){
        List<Map<String,Object>> list = new ArrayList<>();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        if (month == 12)
            today += 31;
        calendar.add(Calendar.DATE, -(today-begin));
        for (int i=begin;i<today;i++){
            Map<String,Object> map = new HashMap<>();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String start = day + " 00:00:00";
            String end = day + " 23:59:59";
            List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("distinct stock"));
            if (i<32){
                map.put("date","12月"+i+"号");
            }else{
                map.put("date","1月"+(i-31)+"号");
            }
//            map.put("date","12月"+i+"号");
            map.put("起始",data.size()>0 ? data.get(0) : null);
            map.put("末尾",data.size()>0 ? data.get(data.size()-1) : null);
            list.add(map);
            calendar.add(Calendar.DATE,1);
        }
        dataVO.setHistory(list);
    }

    private void generateHistoryKMS(DataVO dataVO,Calendar calendar,String name,Integer begin){
        List<Map<String,Object>> list = new ArrayList<>();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        if (month == 12)
            today += 31;
        calendar.add(Calendar.DATE, -(today-begin));
        for (int i=begin;i<today;i++){
            Map<String,Object> map = new HashMap<>();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String start = day + " 00:00:00";
            String end = day + " 23:59:59";
            List<Object> data = dataKmsDao.selectObjs(new QueryWrapper<DataKmsEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("distinct stock"));
            if (i<32){
                map.put("date","12月"+i+"号");
            }else{
                map.put("date","1月"+(i-31)+"号");
            }
//            map.put("date","12月"+i+"号");
            map.put("起始",data.size()>0 ? data.get(0) : null);
            map.put("末尾",data.size()>0 ? data.get(data.size()-1) : null);
            list.add(map);
            calendar.add(Calendar.DATE,1);
        }
        dataVO.setHistory(list);
    }

    private void generateHistoryKMSForMul(DataMulVO dataVO,Calendar calendar,String name,Integer begin){
        List<Map<String,Object>> listMS = new ArrayList<>();
        List<Map<String,Object>> listSN = new ArrayList<>();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        if (month == 12)
            today += 31;
        calendar.add(Calendar.DATE, -(today-begin));
        for (int i=begin;i<today;i++){
            Map<String,Object> mapMS = new HashMap<>();
            Map<String,Object> mapSN = new HashMap<>();
            String day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
            String start = day + " 00:00:00";
            String end = day + " 23:59:59";
            List<Object> dataMS = dataKmsSaleDao.selectObjs(new QueryWrapper<DataKmsSaleEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("distinct month_sale"));

            List<Object> dataSN = dataKmsSaleDao.selectObjs(new QueryWrapper<DataKmsSaleEntity>()
                    .eq("name",name)
                    .ge("time",start)
                    .le("time",end)
                    .select("distinct sold_num"));

//            if (i<31){
//                map.put("date","11月"+i+"号");
//            }else{
//                map.put("date","12月"+(i-30)+"号");
//            }
//            mapMS.put("date","8月"+i+"号");
            if (i<32){
                mapMS.put("date","12月"+i+"号");
            }else{
                mapMS.put("date","1月"+(i-31)+"号");
            }
            mapMS.put("起始",dataMS.size()>0 ? dataMS.get(0) : null);
            mapMS.put("末尾",dataMS.size()>0 ? dataMS.get(dataMS.size()-1) : null);
            listMS.add(mapSN);
//            mapSN.put("date","8月"+i+"号");
            if (i<32){
                mapSN.put("date","12月"+i+"号");
            }else{
                mapSN.put("date","1月"+(i-31)+"号");
            }
            mapSN.put("起始",dataSN.size()>0 ? dataSN.get(0) : null);
            mapSN.put("末尾",dataSN.size()>0 ? dataSN.get(dataSN.size()-1) : null);
            listSN.add(mapSN);
            calendar.add(Calendar.DATE,1);
        }
        dataVO.setHistoryMS(listMS);
        dataVO.setHistorySN(listSN);
    }
    private void generateChange(DataVO dataVO){
        List<Object> stocks = dataVO.getStocks();
        List<Integer> change = dataVO.getChange();
        Integer pre = (stocks.size() == 0 ? null : Integer.parseInt(stocks.get(0).toString()));
        for (int i=1;i<stocks.size();i++){
            Integer curr = Integer.parseInt(stocks.get(i).toString());
            Integer interval = curr-pre;
            change.add(interval);
            pre = curr;
        }
    }

    private void generateChangeForMul(DataMulVO dataMulVO){
        List<Object> stocksMS = dataMulVO.getStocksMS();
        List<Object> stocksSN = dataMulVO.getStocksSN();
        List<Integer> changeMS = dataMulVO.getChangeMS();
        List<Integer> changeSN = dataMulVO.getChangeSN();
        Integer pre = (stocksMS.size() == 0 ? null : Integer.parseInt(stocksMS.get(0).toString()));
        for (int i=1;i<stocksMS.size();i++){
            Integer curr = Integer.parseInt(stocksMS.get(i).toString());
            Integer interval = curr-pre;
            changeMS.add(interval);
            pre = curr;
        }

        pre = (stocksMS.size() == 0 ? null : Integer.parseInt(stocksSN.get(0).toString()));
        for (int i=1;i<stocksSN.size();i++){
            Integer curr = Integer.parseInt(stocksSN.get(i).toString());
            Integer interval = curr-pre;
            changeSN.add(interval);
            pre = curr;
        }
    }


    @Override
    public DataVO getChangeVd(String name) {

        // 获取今天的时间段
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String start = today + " 00:00:00";
        String end = today + " 23:59:59";
        List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
                .eq("name",name)
                .ge(StringUtils.isNotBlank(start),"time",start)
                .le(StringUtils.isNotBlank(end),"time",end)
                .select("distinct stock"));
        DataVO dataVO = new DataVO(name,data);
        generateChange(dataVO);
        return dataVO;
    }

    @Override
    public DataVO getChangeKMS(String name) {
        // 获取用户输入的时间段

        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        String start = today + " 00:00:00";
        String end = today + " 23:59:59";

        List<Object> data = dataKmsDao.selectObjs(new QueryWrapper<DataKmsEntity>()
                .eq("name",name)
                .ge(StringUtils.isNotBlank(start),"time",start)
                .le(StringUtils.isNotBlank(end),"time",end)
                .select("distinct stock"));
        DataVO dataVO = new DataVO(name,data);
        generateChange(dataVO);
        return dataVO;
    }
}