package com.healerjean.proj.utils.gray;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.healerjean.proj.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;


/**
 * 自制工具类
 *
 * @author healerjean
 * @date 2022-09-21 22:27
 */
@Slf4j
public class GrayUtil {


    /**
     * 是否命中灰度
     * 一、灰度业务判断
     * 1、灰度业务不存在 返回：GrayEnum.GrayResEnum.GRAY_NOT_EXIST
     * 2、判断是否灰度关闭，是返回 GrayEnum.GrayResEnum.GRAY_CLOSE;
     * 二、灰度黑白名单判断
     *
     * @param grayBusinessEnum 灰度业务枚举
     * @param grayValue        灰度值
     * @return 灰度开关是否打开
     */
    public static GrayEnum.GrayResEnum hitGray(String grayValue, GrayEnum.GrayBusinessEnum grayBusinessEnum) {

        // 一、灰度业务判断 返回：GrayEnum.GrayResEnum.GRAY_NOT_EXIST
        GrayConfiguration grayConfiguration = SpringUtils.getBean(GrayConfiguration.class);
        Map<String, GrayBizBO> grayBizMap = Optional.ofNullable(grayConfiguration.getGrayBizMap()).orElse(Maps.newHashMap());
        GrayBizBO grayBiz = grayBizMap.get(grayBusinessEnum.getCode());
        if (Objects.isNull(grayBiz)) {
            log.info("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果: false(未配置灰度)", grayBusinessEnum, grayValue);
            return GrayEnum.GrayResEnum.GRAY_NOT_EXIST;
        }


        // 二、灰度黑白名单判断
        // 1、白名单判断,如果在白名单，返回：GrayEnum.GrayResEnum.GRAY_WHITE_TRUE;
        Set<String> whiteUsers = Optional.ofNullable(grayBiz.getWhiteInfos()).orElse(Sets.newHashSet());
        if (whiteUsers.contains(grayValue)) {
            log.info("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：true(白名单命中)", grayBusinessEnum, grayValue);
            return GrayEnum.GrayResEnum.GRAY_WHITE_TRUE;
        }

        // 2、黑白名单判断,如果在白名单，返回：GrayEnum.GrayResEnum.GRAY_BLACK_TRUE;
        Set<String> blackInfos = Optional.ofNullable(grayBiz.getBlackInfos()).orElse(Sets.newHashSet());
        if (blackInfos.contains(grayValue)) {
            log.info("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：false(黑名单命中)", grayBusinessEnum, grayValue);
            return GrayEnum.GrayResEnum.GRAY_BLACK_TRUE;
        }

        // 三、灰度比例判断
        // 3.1、灰度比例不存在，则返回false
        // 3.2、灰度比例计算，命中返回ture，不命中返回false
        boolean isHitHashValue = hitHashValue(grayBiz, grayValue);
        if (isHitHashValue) {
            log.info("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：true(灰度命中)", grayBusinessEnum, grayValue);
            return GrayEnum.GrayResEnum.GRAY_TRUE;
        }
        log.info("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：false(灰度未命中)", grayBusinessEnum, grayValue);
        return GrayEnum.GrayResEnum.GRAY_FALSE;
    }

    /**
     * hitHashValue
     *
     */
    private static boolean hitHashValue(GrayBizBO grayBiz, String grayValue) {
        if (Objects.isNull(grayValue)){
            return false;
        }
        long hashValue = Math.abs(grayValue.hashCode());
        if (NumberUtil.isLong(grayValue)) {
            hashValue =  Long.parseLong(grayValue);
        }
        long rate = hashValue % grayBiz.getAmount() + 1;
        return rate <= grayBiz.getRate();
    }


}
