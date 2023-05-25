package com.jd.merchant.mq.route.util;

import com.alibaba.fastjson.JSON;
import com.jd.merchant.mq.route.bean.GrayInsuranceBusinessDto;
import com.jd.merchant.mq.route.config.DuccBypassInsuranceConfiguration;
import com.jd.merchant.mq.route.enums.GrayEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


/**
 * 自制工具类
 *
 * @author healerjean
 * @date 2022-09-21 22:27
 */
@Service
@Slf4j
public class GrayUtil {

    /**
     * duccBypassInsuranceConfiguration
     */
    @Resource
    private DuccBypassInsuranceConfiguration duccBypassInsuranceConfiguration;

    /**
     * 是否命中灰度
     * 1、判断灰度开关是否打开
     * 2、判断灰度对象是否在灰度白名单中
     * 3、判断是否命中灰度百分比
     *
     * @param grayBusinessEnum 灰度业务枚举
     * @param grayObject       灰度值
     * @return 灰度开关是否打开
     */
    public <T> boolean hitGray(GrayEnum.GrayBusinessEnum grayBusinessEnum, T grayObject) {
        if (grayObject instanceof String) {
            return getInsuranceVendorGraySwitch(grayBusinessEnum, (String) grayObject);
        }
        return false;
    }



    /**
     * ducc险种灰度开关 (定制化)
     * 1、如果ducc配置为空或入参为空，则返回false
     * 2、灰度开关判断
     * 2.1、如果灰度对象为空，则返回false
     * 2.2、如果灰度开关状态不存在或者开关关闭，则返回false
     * 2.3、如果灰度开关状态显示全量，则返回true
     * 3、商家白名单判断,如果商家在白名单，则返回true
     * 4、灰度比例判断
     * 4.1、灰度比例不存在，则返回false
     * 4.2、灰度比例计算，命中返回ture，不命中返回false
     *
     * @param insuranceId 险种Id
     * @return 路由切换开关
     */
    private boolean getInsuranceVendorGraySwitch(GrayEnum.GrayBusinessEnum grayBusinessEnum, String vendorId) {
        Map<String, GrayInsuranceBusinessDto> bypassInsuranceSwitchMap = duccBypassInsuranceConfiguration.getBypassGrayVendorMap();

        // 1、如果ducc配置为空或入参为空，则返回 灰度关闭
        if (CollectionUtils.isEmpty(bypassInsuranceSwitchMap)) {
            return false;
        }

        //ducc 这个小傻瓜不支持直接转对象
        String json = JSON.toJSONString(bypassInsuranceSwitchMap.get(grayBusinessEnum.getInsuranceId())) ;
        GrayInsuranceBusinessDto grayVendor =JSON.parseObject(json, GrayInsuranceBusinessDto.class);

        // 2.1、如果灰度对象为空，则返回false
        if (Objects.isNull(grayVendor)) {
            return false;
        }

        // 2.2、如果灰度开关状态不存在或者开关关闭，则返回false
        String graySwitchCode = grayVendor.getGraySwitchCode();
        GrayEnum.GraySwitchEnum graySwitchEnum = GrayEnum.GraySwitchEnum.toGraySwitchEnum(graySwitchCode);
        if (Objects.isNull(graySwitchEnum) || GrayEnum.GraySwitchEnum.GRAY_CLOSE == graySwitchEnum) {
            return false;
        }

        // 2.3、如果灰度开关状态显示全量，则返回true
        if (GrayEnum.GraySwitchEnum.ALL_PERCENT == graySwitchEnum) {
            return true;
        }
        if (GrayEnum.GraySwitchEnum.GRAY_PERCENT != graySwitchEnum) {
            return false;
        }

        // 3、商家白名单判断,如果商家在白名单，则返回true
        Set<String> whiteVendorIds = grayVendor.getWhiteVendorIds();
        if (!CollectionUtils.isEmpty(whiteVendorIds) && whiteVendorIds.contains(vendorId)) {
            log.info("[GrayUtil#getInsuranceVendorGraySwitch] 商家白名单命中，商家id:{}, 白名单商家集合:{}：灰度状态：true", vendorId, JSON.toJSONString(whiteVendorIds));
            return true;
        }

        // 4.1、灰度比例不存在，则返回false
        Integer grayPercent = grayVendor.getGrayPercent();
        Integer grayPercentAmount = grayVendor.getGrayPercentAmount();
        if (Objects.isNull(grayPercent) || Objects.isNull(grayPercentAmount)) {
            return false;
        }
        // 4.2、灰度比例计算，命中返回ture，不命中返回false
        int hashCode = vendorId.hashCode();
        int rate = Math.abs(hashCode) % grayPercentAmount;
        if (rate <= grayPercent) {
            log.info("[GrayUtil#getInsuranceVendorGraySwitch] 商家Id:{}，命中灰度:{} grayPercent:{}, grayPercentAmount:{}， 灰度状态：true", vendorId, rate, grayPercent, grayPercentAmount);
            return true;
        }
        return false;
    }

}
