package com.example.sale.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.sale.dto.IpDTO;
import com.example.sale.model.Person;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;


/**
 * @author: wljlsmz
 * @date: 2022/11/15 10:28
 * @description: ip工具类
 */
@Slf4j
public class IpUtils {

    /**
     * 本地环回地址
     */
    private static final String LOCAL_IP = "127.0.0.1";

    /**
     * 未知
     */
    private static final String UNKNOWN = "unknown";

    public static String getIpAddr(HttpServletRequest request) {

        if (request == null) {
            return UNKNOWN;
        }

        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (StringUtils.isBlank(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? LOCAL_IP : ip;
    }

    public static String getCityInfo(String ip) throws Exception {

        if (!Util.isIpAddress(ip)) {
            log.error("错误: 无效的ip地址");
            return null;
        }

        InputStream is = new PathMatchingResourcePatternResolver().getResources("ip2region.db")[0].getInputStream();
        File target = new File("ip2region.db");
        FileUtils.copyInputStreamToFile(is, target);
        is.close();

        if (StringUtils.isEmpty(String.valueOf(target))) {
            log.error("错误: 无效的ip2region.db文件");
            return null;
        }

        DbSearcher searcher = new DbSearcher(new DbConfig(), String.valueOf(target));

        try {
            DataBlock dataBlock = (DataBlock) searcher.getClass().getMethod("btreeSearch", String.class).invoke(searcher, ip);

            String ipInfo = dataBlock.getRegion();
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }

            return ipInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IpDTO getRegion(String ip) throws IOException {
        String url = "http://ip-api.com/json/"+ip;
        HttpClient httpClient = new HttpClient();
        GetMethod getMethod = new GetMethod(url);
        String res_str = "";
        JSONObject res_obj = null;
        String country = "1";
        String region = "2";
        int code = httpClient.executeMethod(getMethod);
        if (code == 200){
            res_str = getMethod.getResponseBodyAsString();
            res_obj = JSON.parseObject(res_str);
            country = (String) res_obj.get("country");
            region = (String) res_obj.get("regionName");
        }
        return new IpDTO(country,region);
    }
}
