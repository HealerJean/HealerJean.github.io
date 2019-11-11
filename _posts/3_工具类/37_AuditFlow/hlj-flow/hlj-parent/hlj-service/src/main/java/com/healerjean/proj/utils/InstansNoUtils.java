package com.healerjean.proj.utils;

import java.util.UUID;

/**
 * @author HealerJean
 * @ClassName InstansNoUtils
 * @date 2019/11/11  16:38.
 * @Description
 */
public class InstansNoUtils {


    public static String uniqueSeq() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
