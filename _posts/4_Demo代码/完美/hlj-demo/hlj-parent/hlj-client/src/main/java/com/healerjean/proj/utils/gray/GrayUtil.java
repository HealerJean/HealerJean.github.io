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
     * hitGray
     *
     * @param grayBusinessMap grayBusinessMap
     * @return {@link GrayEnum.GrayResEnum}
     */
    public static GrayEnum.GrayResEnum hitGrayRules(GrayEnum.GrayStrategyEnum grayStrategyEnum, Map<GrayEnum.GrayBusinessEnum, String> grayBusinessMap) {
        // 全部命中
        if (GrayEnum.GrayStrategyEnum.ALL_HIT == grayStrategyEnum){
            for (Map.Entry<GrayEnum.GrayBusinessEnum, String> entry : grayBusinessMap.entrySet()) {
                GrayEnum.GrayResEnum grayResEnum = hitGrayRule(entry.getKey(), entry.getValue());
                if (!grayResEnum.getHitFlag()) {
                    return grayResEnum;
                }
            }
            return GrayEnum.GrayResEnum.GRAY_TRUE;
        }

        // 任一命中
        if (GrayEnum.GrayStrategyEnum.ANY_HIT == grayStrategyEnum){
            for (Map.Entry<GrayEnum.GrayBusinessEnum, String> entry : grayBusinessMap.entrySet()) {
                GrayEnum.GrayResEnum grayResEnum = hitGrayRule(entry.getKey(), entry.getValue());
                if (grayResEnum.getHitFlag()) {
                    return grayResEnum;
                }
            }
            return GrayEnum.GrayResEnum.GRAY_FALSE;
        }
        throw new RuntimeException("灰度策略不支持");
    }




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
    public static GrayEnum.GrayResEnum hitGrayRule(GrayEnum.GrayBusinessEnum grayBusinessEnum, String grayValue) {
        // 一、灰度业务判断 返回：GrayEnum.GrayResEnum.GRAY_NOT_EXIST
        GrayConfiguration grayConfiguration = SpringUtils.getBean(GrayConfiguration.class);
        Map<String, GrayBizBO> grayBizMap = Optional.ofNullable(grayConfiguration.getGrayBizMap()).orElse(Maps.newHashMap());
        GrayBizBO grayBiz = grayBizMap.get(grayBusinessEnum.getCode());
        if (Objects.isNull(grayBiz)) {
            if (log.isDebugEnabled()) {
                log.debug("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果: false(未配置灰度)", grayBusinessEnum, grayValue);
            }
            return GrayEnum.GrayResEnum.GRAY_NOT_EXIST;
        }


        // 二、灰度黑白名单判断
        // 1、白名单判断,如果在白名单，返回：GrayEnum.GrayResEnum.GRAY_WHITE_TRUE;
        Set<String> whiteUsers = Optional.ofNullable(grayBiz.getWhiteInfos()).orElse(Sets.newHashSet());
        if (whiteUsers.contains(grayValue)) {
            if (log.isDebugEnabled()) {
                log.debug("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：true(白名单命中)", grayBusinessEnum, grayValue);
            }
            return GrayEnum.GrayResEnum.GRAY_WHITE_TRUE;
        }

        // 2、黑白名单判断,如果在白名单，返回：GrayEnum.GrayResEnum.GRAY_BLACK_TRUE;
        Set<String> blackInfos = Optional.ofNullable(grayBiz.getBlackInfos()).orElse(Sets.newHashSet());
        if (blackInfos.contains(grayValue)) {
            if (log.isDebugEnabled()) {
                log.debug("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：false(黑名单命中)", grayBusinessEnum, grayValue);
            }
            return GrayEnum.GrayResEnum.GRAY_BLACK_TRUE;
        }

        // 三、灰度比例判断
        // 3.1、灰度比例不存在，则返回false
        long grayPercent = grayBiz.getRate();
        long grayPercentAmount = grayBiz.getAmount();

        // 3.2、灰度比例计算，命中返回ture，不命中返回false
        long rate = Math.abs(hashValue(grayBusinessEnum, grayValue)) % grayPercentAmount + 1;
        if (rate <= grayPercent) {
            if (log.isDebugEnabled()) {
                log.debug("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：true(灰度命中)", grayBusinessEnum, grayValue);
            }
            return GrayEnum.GrayResEnum.GRAY_TRUE;
        }
        if (log.isDebugEnabled()) {
            log.debug("[GrayUtil#hitGray] grayBusinessEnum:{}, grayValue:{} 灰度结果：false(灰度未命中)", grayBusinessEnum, grayValue);
        }
        return GrayEnum.GrayResEnum.GRAY_FALSE;
    }


    /**
     * hashValue
     *
     * @param grayBusinessEnum grayBusinessEnum
     * @param grayValue        grayValue
     * @return {@link Integer}
     */
    private static Long hashValue(GrayEnum.GrayBusinessEnum grayBusinessEnum, String grayValue) {
        if (Objects.isNull(grayValue)){
            return 0L;
        }
        if (NumberUtil.isLong(grayValue)) {
            return Long.valueOf(grayValue);
        }
        return (long) grayValue.hashCode();
    }

}