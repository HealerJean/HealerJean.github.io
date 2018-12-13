package com.hlj.IgnoreNullBean;

import org.junit.platform.commons.util.StringUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * ���� ��HealerJean
 * ���� ��2018/12/13  ����10:55.
 * ������ ��ֹ�ǿ��ֶΣ��������� new BigDecimal(string))�н��б���
 */
public class JavaBeanUtil {


    /**
     *
     * @param object ԭʼ����JavaBean
     * @param originFieldName ԭʼ���� �ֶ����� ���磺name
     * @param newObject �¸��Ƶ�JavaBean
     * @param newFilldName �¸�ֵ���ֶ����� ���� nameNow
     */
    public static  void setFieldValue(Object object,String originFieldName,Object newObject,String newFilldName) {


        try {
            Field field = object.getClass().getDeclaredField(originFieldName);
            field.setAccessible(true);

            Field newfield = newObject.getClass().getDeclaredField(newFilldName);
            newfield.setAccessible(true);
            String newfieldType=newfield.getGenericType().toString();
            if (field.get(object) != null && StringUtils.isNotBlank(field.get(object).toString())) {
                String value = field.get(object).toString();
                switch (newfieldType){
                    case "class java.lang.Integer":
                        newfield.set(newObject, Integer.valueOf(value));
                        break;
                    case "class java.lang.Long":
                        newfield.set(newObject, Long.valueOf(value));
                        break;
                    case "class java.math.BigDecimal":
                        newfield.set(newObject, new BigDecimal(Double.valueOf(value)) );
                        break;
                    case "class java.util.Date":
                     Date date=new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK).parse(value);
                     newfield.set(newObject, date);
                     break;
                    default:
                        break;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
