package com.example.sale.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.sale.common.BusinessException;
import com.example.sale.constant.ResultConstants;
import com.example.sale.dao.DataDao;
import com.example.sale.dto.CommonDTO;
import com.example.sale.dto.FourDTO;
import com.example.sale.entity.DataEntity;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.vo.DataVO;
import com.example.sale.vo.SvVO;
import com.example.sale.vo.ThreeVO;
import com.example.sale.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.example.sale.dao.SvDao;
import com.example.sale.entity.SvEntity;
import com.example.sale.service.SvService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


@Service("svService")
public class SvServiceImpl extends ServiceImpl<SvDao, SvEntity> implements SvService, ResultConstants {

    @Autowired
    private SvDao svDao;

    @Autowired
    private DataDao dataDao;

    @Override
    public List<SvVO> getData(Integer target) {
        List<SvEntity> list = svDao.selectList(new LambdaQueryWrapper<SvEntity>().eq(target!=null,SvEntity::getId,target));
        List<SvVO> res = new ArrayList<>();
        for(SvEntity entity:list){
            res.add(generateVO(entity,target!=null&&target==189));
        }
        return res;
    }

    @Override
    public List<SvVO> getData1(Integer role) {
        List<Integer> ids = new ArrayList<>();
        List<SvVO> res = new ArrayList<>();
        if (role == 6){
//            ids = Arrays.asList(190,191,192,193,194,195,196,197);
        }else if (role == 9){
//            ids = Arrays.asList(190,191,194,196,197);
        }


        for (Integer id:ids) {
            SvEntity entity = svDao.selectById(id);
            res.add(generateVO(entity,true));
        }
        return res;
    }

    @Override
    public void edit(CommonDTO commonDTO) {
        SvEntity entity = svDao.selectById(commonDTO.getId());
        if (entity == null)
            throw new BusinessException(NOT_EXIST);
        entity.setSale(commonDTO.getSale());
        svDao.updateById(entity);
    }

    @Override
    public List<ThreeVO> countFour(FourDTO fourDTO) {
        List<ThreeVO> res = new ArrayList<>();
        String a = fourDTO.getVd1();
        String b = fourDTO.getVd2();
        String d = fourDTO.getKms1();
        String e = fourDTO.getKms2();
//        if (StringUtils.isBlank(a) || StringUtils.isBlank(b) || StringUtils.isBlank(d) || StringUtils.isBlank(e))
//            throw new BusinessException("四项都不能为空！");
        String[] aa = a.split("\n");
        String[] bb = b.split("\n");
        String[] dd = d.split("\n");
        String[] ee = e.split("\n");
        if (!(aa.length == bb.length && bb.length == dd.length && dd.length == ee.length))
            throw new BusinessException("四项数字个数不一致！");
        for (int i=0;i<aa.length;i++){
            Integer A = Integer.parseInt(aa[i]);
            Integer B = Integer.parseInt(bb[i]);
            Integer D = Integer.parseInt(dd[i]);
            Integer E = Integer.parseInt(ee[i]);
            Integer AB = A-B;
            Integer DE = D-E;
            res.add(new ThreeVO(i+1,AB,DE,AB+DE));
        }
        return res;
    }


    private SvVO generateVO(SvEntity entity,boolean flag){
        SvVO svVO = new SvVO();
        BeanUtils.copyProperties(entity,svVO);
        Integer sale = entity.getSale();
        Integer a = sale/entity.getBig();
        Integer b = sale/entity.getSmall();
        svVO.setNum(a+"~"+b);
        // 对普通用户隐藏销量
//        if (flag) svVO.setSale(0);
//        // 合影可见增幅
//        if (svVO.getId()>=190){
//            Calendar calendar = Calendar.getInstance();
//            int today_day = calendar.get(Calendar.DAY_OF_MONTH);
//            String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
//            calendar.add(Calendar.DATE, -(today_day-20));
//            String start_day = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
////        int month = calendar.get(Calendar.MONTH);
////        if (month == 11)
////            today += 30;
//            String day_start = start_day + " 00:00:00";
//            String day_end = today + " 23:59:59";
//            List<Object> data = dataDao.selectObjs(new QueryWrapper<DataEntity>()
//                    .eq("name", svVO.getName())
//                    .ge("time",day_start)
//                    .le("time",day_end)
//                    .select("distinct stock"));
//
//            List<Integer> change = new ArrayList<>();
//            Integer pre = (data.size() == 0 ? null : Integer.parseInt(data.get(0).toString()));
//            for (int i=1;i<data.size();i++){
//                Integer curr = Integer.parseInt(data.get(i).toString());
//                Integer interval = curr-pre;
//                change.add(interval);
//                pre = curr;
//            }
//            svVO.setChange(change);
//        }
        return svVO;
    }
}