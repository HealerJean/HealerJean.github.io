package com.healerjean.proj.utils.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlKeyword;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.api.R;
import com.healerjean.proj.common.data.bo.OrderByBO;
import com.healerjean.proj.common.enums.PageEnum;
import com.healerjean.proj.data.po.UserDemo;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Predicate;

/**
 * MybatisPlusUtil
 *
 * @author zhangyujin
 * @date 2023/6/5  15:23.
 */
@Slf4j
public class MybatisPlusUtil {


    /**
     * fieldValues
     *
     * @param fieldValues  fieldValues
     * @param queryWrapper queryWrapper
     * @param aClass       aClass
     * @param <T>          <T>
     */
    public static <T> void fieldValues(List<String> fieldValues, LambdaQueryWrapper<T> queryWrapper, Class<T> aClass) {
        if (CollectionUtils.isEmpty(fieldValues)) {
            return;
        }
        Predicate<TableFieldInfo> predicate = null;
        for (String field : fieldValues) {
            predicate = predicate == null ? p -> p.getColumn().equals(field) : predicate.or(p -> p.getColumn().equals(field));
        }
        queryWrapper.select(aClass, predicate);
    }





}
