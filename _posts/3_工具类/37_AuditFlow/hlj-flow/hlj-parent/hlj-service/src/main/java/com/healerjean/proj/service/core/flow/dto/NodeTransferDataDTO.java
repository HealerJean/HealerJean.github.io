package com.healerjean.proj.service.core.flow.dto;

import com.healerjean.proj.dto.user.LoginUserDTO;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author HealerJean
 * @ClassName NodeTransferDataDTO
 * @date 2019/11/11  16:09.
 * @Description
 */
@Data
@Accessors(chain = true)
public class NodeTransferDataDTO {
    /**
     * 处理Json
     */
    private String data;
    /**
     * 是否执行下一个节点
     */
    private Boolean next = false;


    public static NodeTransferDataDTO instanct(String data) {
        return new NodeTransferDataDTO().setData(data);
    }

}
