package com.fintech.scf.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName DecimalUtils
 * @date 2019/7/4  20:22.'==========================================================
 * @Description
 */
public class DecimalUtils {

    public static final DecimalFormat FORMAT = new DecimalFormat("#,##0.00");

    /**
     * @param decimal  71015000009826
     * @return 71,015,000,009,826.00
     */
    public static String format(BigDecimal decimal ,DecimalFormat format) {
        return format.format(decimal);
    }



}
