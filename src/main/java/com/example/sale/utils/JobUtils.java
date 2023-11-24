package com.example.sale.utils;


import com.example.sale.constant.GroupConstants;
import com.example.sale.dao.DataDao;
import com.example.sale.dao.DataKmsDao;
import com.example.sale.dao.DataKmsSaleDao;
import com.example.sale.entity.DataEntity;
import com.example.sale.entity.DataKmsEntity;
import com.example.sale.entity.DataKmsSaleEntity;
import com.example.sale.model.Person;
import com.example.sale.service.DataService;
import com.example.sale.service.SkService;
import com.example.sale.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class JobUtils {

    @Autowired
    private UserService userService;

    @Autowired
    private DataDao dataDao;

    @Autowired
    private DataKmsDao dataKmsDao;

    @Autowired
    private DataKmsSaleDao dataKmsSaleDao;

    @Autowired
    private SkService skService;

    /**
     * fixedRate：每间隔2秒执行一次任务
     * 注意，默认情况下定时任务是在同一线程同步执行的，如果任务的执行时间（如5秒）大于间隔时间，则会等待任务执行结束后直接开始下次任务
     * fixedDelay：每次延时2秒执行一次任务
     * 注意，这里是等待上次任务执行结束后，再延时固定时间后开始下次任务
     */
//    @Scheduled(fixedDelay = 60000)
//    public void task0() {
//        List<Person> data = skService.updateData();
//        for (Person person:data){
//            DataEntity entity = new DataEntity();
//            BeanUtils.copyProperties(person,entity);
//            dataDao.insert(entity);
//        }
//    }
    @Scheduled(fixedDelay = 60000)
    public void task0() {
        List<Person> data = userService.getDataVd(GroupConstants.urls_sk_vd,GroupConstants.names_sk);
        for (Person person:data){
            DataEntity entity = new DataEntity();
            BeanUtils.copyProperties(person,entity);
            dataDao.insert(entity);
        }
    }
//
    @Scheduled(fixedDelay = 60000)
    public void task1() {
        List<Person> data = userService.getDataKMS(GroupConstants.urls_sk_kms,GroupConstants.names_sk);
        for (Person person:data){
            DataKmsEntity entity = new DataKmsEntity();
            BeanUtils.copyProperties(person,entity);
            dataKmsDao.insert(entity);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void task2() {
        List<Person> data = userService.getDataKMSForSale(GroupConstants.urls_sk_kms,GroupConstants.names_sk);
        for (Person person:data){
            DataKmsSaleEntity entity = new DataKmsSaleEntity();
            BeanUtils.copyProperties(person,entity);

            dataKmsSaleDao.insert(entity);
        }
    }

/**
 *  另一组任务
 */

    @Scheduled(fixedDelay = 60000)
    public void task3() {
        List<Person> data = userService.getDataVdForMul(GroupConstants.urls_dz_vd,GroupConstants.names_dz_2);
        for (Person person:data){
            DataEntity entity = new DataEntity();
            BeanUtils.copyProperties(person,entity);
            dataDao.insert(entity);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void task4() {
        List<Person> data = userService.getDataKMS(GroupConstants.urls_dz_kms,GroupConstants.names_dz);
        for (Person person:data){
            DataKmsEntity entity = new DataKmsEntity();
            BeanUtils.copyProperties(person,entity);
            dataKmsDao.insert(entity);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void task5() {
        List<Person> data = userService.getDataKMSForSale(GroupConstants.urls_dz_kms,GroupConstants.names_dz);
        for (Person person:data){
            DataKmsSaleEntity entity = new DataKmsSaleEntity();
            BeanUtils.copyProperties(person,entity);

            dataKmsSaleDao.insert(entity);
        }
    }

    /**
     *  另一组任务
     */

    @Scheduled(fixedDelay = 60000)
    public void task6() {
        List<Person> data = userService.getDataVdForMul(GroupConstants.urls_127_vd,GroupConstants.names_127_2);
        for (Person person:data){
            DataEntity entity = new DataEntity();
            BeanUtils.copyProperties(person,entity);
            dataDao.insert(entity);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void task7() {
        List<Person> data = userService.getDataKMS(GroupConstants.urls_127_kms,GroupConstants.names_127);
        for (Person person:data){
            DataKmsEntity entity = new DataKmsEntity();
            BeanUtils.copyProperties(person,entity);
            dataKmsDao.insert(entity);
        }
    }

    @Scheduled(fixedDelay = 60000)
    public void task8() {
        List<Person> data = userService.getDataKMSForSale(GroupConstants.urls_127_kms,GroupConstants.names_127);
        for (Person person:data){
            DataKmsSaleEntity entity = new DataKmsSaleEntity();
            BeanUtils.copyProperties(person,entity);

            dataKmsSaleDao.insert(entity);
        }
    }
}
