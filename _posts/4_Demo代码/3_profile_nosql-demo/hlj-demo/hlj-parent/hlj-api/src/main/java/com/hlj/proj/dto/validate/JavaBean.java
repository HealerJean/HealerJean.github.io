package com.healerjean.proj.dto.validate;

import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.validate.anno.GreaterLess;
import com.healerjean.proj.validate.anno.NameInclude;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author HealerJean
 * @version 1.0v
 * @Description 注意点
 * 1、 下面出了判断空的注解之外，必须有值才回校验
 * 2、 所有校验属性必须加组，方便阅读
 * @ClassName JavaBean
 * @date 2019/4/17  14:08.
 */
@Data
public class JavaBean extends BaseBean {


    @NotBlank(message = "name 为空 ", groups = ValidateGroup.HealerJean.class)
    @Size(min = 1, max = 5, message = "name  @Size(min = 1,max = 5  字符串长度 最低为1 最大为5", groups = ValidateGroup.HealerJean.class)
    private String name;

    @Size(min = 1, max = 2, message = "list @Size(min = 1,max = 2 集合大小 最低为1 最大为2", groups = ValidateGroup.HealerJean.class)
    private List<String> list;

    @Length(min = 1, max = 5, message = "@Length(min = 1,max = 5 字符串长度 最低为1 最大为5", groups = ValidateGroup.HealerJean.class)
    private String strLength;

    @Min(value = 5, message = "strNum  @Min(value = 5,message =  字符串（数字的字符串大小判断）【数字类型的变量都可以】", groups = ValidateGroup.HealerJean.class)
    private String strNum;

    @Range(min = 1, max = 10, message = "strRange @Range(min = 1,max = 10  最小为1，最大为10  ", groups = ValidateGroup.HealerJean.class)
    private String strRange;

    @DecimalMin(value = "100.1", message = "小数值的判断，最小为 100.1", groups = ValidateGroup.HealerJean.class)
    private String strDecimal;

    @Digits(integer = 2, fraction = 2, message = "strDigts  @Digits(integer = 2,fraction = 2  整数最高2位，小数最高2位", groups = ValidateGroup.HealerJean.class)
    private String strDigts;

    @AssertFalse(message = " @AssertFalse 必须为false ", groups = ValidateGroup.HealerJean.class)
    private Boolean assertFalse;

    /**
     * 内部对象校验
     */
    @Valid
    @NotNull(message = "内部对象不能为空", groups = ValidateGroup.HealerJean.class)
    private InnerBean innerBean;

    /**
     * 自定义注解校验
     */
    @NameInclude(message = "类型必须是type value必须是HealerJean", type = "Mail", groups = {ValidateGroup.HealerJean.class})
    @NotBlank(message = "自定义校验不能为空", groups = {ValidateGroup.HealerJean.class})
    private String myName;

    @NotNull(message = "myDec不能为空", groups = {ValidateGroup.HealerJean.class})
    @GreaterLess(min = "1.1", max = "10.5", message = "必须大于1.1 并且小于 10.5 ", groups = {ValidateGroup.HealerJean.class})
    private BigDecimal myDec;


    // /**测试非Null下,其他注解属性无需 加 group*/
    @NotBlank(message = "testGroup不能为空", groups = {ValidateGroup.HealerJean.class})
    @Length(max = 5, message = "testGroup长度不能超过5")
    private String testGroup ;

}
