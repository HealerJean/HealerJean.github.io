
import org.junit.platform.commons.util.StringUtils;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/12/13  上午10:55.
 * 类描述 防止非空字段，在类似于 new BigDecimal(string))中进行报错
 */
public class JavaBeanUtil {


    /**
     *
     * @param object 原始数据JavaBean
     * @param originFieldName 原始数据 字段名字 比如：name
     * @param newObject 新复制的JavaBean
     * @param newFilldName 新赋值的字段名字 比如 nameNow
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
