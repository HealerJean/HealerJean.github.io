package com.healerjean.proj.util.mail.dto;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author zhangyujin
 * @date 2021/12/15  4:58 下午.
 * @description
 */

public class ExportFiledBean {
    private Field field;
    private List<Field> listField;

    public ExportFiledBean(Field field, List<Field> listField) {
        this.field = field;
        this.listField = listField;
    }

    public ExportFiledBean(Field field) {
        this.field = field;
        this.listField = null;
    }

    public Field getField() {
        return this.field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public List<Field> getListField() {
        return this.listField;
    }

    public void setListField(List<Field> listField) {
        this.listField = listField;
    }
}
