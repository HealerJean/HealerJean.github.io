package com.hlj.proj.dto.validate;

import com.hlj.proj.common.group.ValidateGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BaseBean
 * @date 2019/6/11  16:38.
 * @Description
 */
@Data
public class BaseBean {

    @NotBlank(message = "父类String 不能为空", groups = ValidateGroup.HealerJean.class)
    private String fatherString;

}
