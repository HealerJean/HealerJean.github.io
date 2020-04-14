package com.hlj.proj.dto.validate;

import com.hlj.proj.common.group.ValidateGroup;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName InnerBean
 * @date 2019/6/11  16:39.
 * @Description
 */
@Data
public class InnerBean {

    @NotBlank(message = "innerBean不能为空", groups = ValidateGroup.HealerJean.class)
    @Length(max = 2, message = "innerNname 长度最长为2", groups = ValidateGroup.HealerJean.class)
    private String innerNname;
}
