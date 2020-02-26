package com.healerjean.proj.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author HealerJean
 * @ClassName ItemUtils
 * @date 2019/10/16  11:15.
 * @Description
 */
public class ItemUtils {

    /**
     * 根据淘宝标准链接获取 id
     *
     * @param url https://detail.tmall.com/item.htm?id=544942589965
     *            https://detail.tmall.com/item.htm?id=544659483197&ali_trackid=2:mm_122329023_44562910_474398289:1530239563_268_1038162022&spm=a2e2e.10720394/c.90200100.1.1a03704cMPaGH3&pvid=23116944&skuId=3671351551757
     * @return 544942589965
     */
    public static Long getItemId(String url) {
        String[] arrSplit = url.split("&");
        for (String strSplit : arrSplit) {
            if (strSplit.contains("id=")) {
                String[] finalSplit = strSplit.split("=");
                return Long.valueOf(finalSplit[1]);
            }
        }
        return null;
    }

    /**
     * 长连接-获取淘宝商品的url
     * https://detail.tmall.com/item.htm?id=544659483197&ali_trackid=2:mm_122329023_44562910_474398289:1530239563_268_1038162022&spm=a2e2e.10720394/c.90200100.1.1a03704cMPaGH3&pvid=23116944&skuId=3671351551757
     *
     * @return https://detail.tmall.com/item.htm?id=544659483197
     */
    public static String getItemUrl(String url) {
        String[] arrSplit = url.split("&");
        for (String strSplit : arrSplit) {
            return strSplit;
        }
        return null;
    }


    /**
     * https://uland.taobao.com/quan/detail?sellerId=1699819907&activityId=9602a736ccff4ebb95b59a463e592a4b
     *
     * @return 9602a736ccff4ebb95b59a463e592a4b
     */
    public static String getActivityId(String url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return url.substring(url.indexOf("activityId=") + 11, url.length());
    }

    public static void main(String[] args) {
        System.out.println(getActivityId("https://uland.taobao.com/quan/detail?sellerId=628189716&activityId=797074a52e054f0eaefc2874d428fafc"));
    }


    /**
     * 获取推广位第三段
     */
    public static Long alimamaAdzoneTo3Long(String adzoneId) {
        return (Long.valueOf(adzoneId.split("_")[3]));
    }


    /**
     * 优惠券链接
     */
    public static String getCouponUrl(Long shopId, String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return null;
        }
        return "https://uland.taobao.com/quan/detail?sellerId=" + shopId + "&activityId=" + activityId;
    }

    /**
     * 二合一链接
     */
    public static String getClickCouponUrl(String activityId, String alimamaAdzone, Long itemId) {
        if (StringUtils.isBlank(activityId)) {
            return null;
        }
        return "https://uland.taobao.com/coupon/edetail?activityId=" + activityId + "&pid=" + alimamaAdzone + "&itemId=" + itemId;
    }
}
