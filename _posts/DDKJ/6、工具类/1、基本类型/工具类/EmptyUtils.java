package com.duodian.youhui.admin.utils;

import java.util.Collection;

public final class EmptyUtils {
    public final static boolean isEmpty(String str) {
        return str == null || str.length() <= 0;
    }

    public final static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

}
