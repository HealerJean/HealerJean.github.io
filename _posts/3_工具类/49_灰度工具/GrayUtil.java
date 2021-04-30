package com.hlj.util.z028_灰度工具;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 自制工具类，还未战斗过
 */
@Service
@Slf4j
public class GrayUtil {

    /**
     * TODO 模拟数据 从配置中心获取
     */
    public static final ImmutableMap<String, List<?>> WHITE_MAP = ImmutableMap.of(
            GrayEnum.GrayBusinessEnum.TOPIC_CHANGE.getCode(), ImmutableList.of(10, 81),
            GrayEnum.GrayBusinessEnum.HELMET_ONLINE.getCode(), ImmutableList.of("10", "81")
    );


    /**
     * 判断所属业务灰度是否打开
     *
     * @param grayBusinessEnum 灰度业务枚举
     * @return
     */
    public static boolean graySwitch(GrayEnum.GrayBusinessEnum grayBusinessEnum) {
        //TODO 正常情况下是需要 从配置中心获取开关数据，如果配置中心不存在的时候，根据业务属性选择默认值
        return grayBusinessEnum.getDefaultSwitch();
    }


    /**
     * 是否命中灰度白名单
     *
     * @return
     */
    public static <T> boolean hitGrayWhiteList(GrayEnum.GrayBusinessEnum grayBusinessEnum, T grayObject) {
        if (!grayBusinessEnum.isWhiteSupport()) {
            return false;
        }

        // TODO 根据业务属性，判断grayObject 是何种类型，然后判断该业务是否支持白名单，如果支持，则从配置中心获取白名单数据，并判断是否包含
        return WHITE_MAP.get(grayBusinessEnum.getCode()).contains(grayObject);
    }


    /**
     * 是否命中灰度百分比
     *
     * @param grayValue
     * @param percent
     * @param grayPercentEnum
     * @return
     */
    public static boolean hitGrayPercent(int grayValue, int percent, GrayEnum.GrayPercentEnum grayPercentEnum) {
        int rate = Math.abs(grayValue) % grayPercentEnum.getCode();
        return rate <= percent;
    }


    /**
     * 是否命中灰度
     * 1、判断灰度开关是否打开
     * 2、判断灰度对象是否在灰度白名单中
     * 3、判断是否命中灰度百分比
     *
     * @param grayBusinessEnum
     * @param grayObject
     * @param percent
     * @param grayPercentEnum
     * @return
     */
    public static <T> boolean hitGray(GrayEnum.GrayBusinessEnum grayBusinessEnum, T grayObject, int percent, GrayEnum.GrayPercentEnum grayPercentEnum) {
        // 1、判断灰度开关是否打开
        boolean gray = graySwitch(grayBusinessEnum);
        if (!gray) {
            log.info("GrayUtil【{}】灰度开关状态：{}", grayBusinessEnum, gray);
            return false;
        }

        // 2、判断灰度对象是否在灰度白名单中
        gray = hitGrayWhiteList(grayBusinessEnum, grayObject);
        if (gray) {
            log.info("GrayUtil【{}】灰度对象:{}, 白名单匹配成功", grayBusinessEnum, grayObject);
            return true;
        }
        // 3、判断是否命中灰度百分比
        gray = hitGrayPercent(grayObject.hashCode(), percent, grayPercentEnum);
        if (gray) {
            log.info("GrayUtil【{}】灰度对象:{}, 命中灰度比分[{},{}] ", grayBusinessEnum, grayObject, grayPercentEnum.getCode(), percent);
            return true;
        }

        return false;
    }



}
