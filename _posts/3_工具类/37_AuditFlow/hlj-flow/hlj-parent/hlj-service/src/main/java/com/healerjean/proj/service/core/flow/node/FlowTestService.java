package com.healerjean.proj.service.core.flow.node;

import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.service.core.flow.dto.NodeTransferDataDTO;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName FlowTestService
 * @date 2019/11/11  19:35.
 * @Description
 */
@Service
public class FlowTestService {

    public void startWorkNode(String flowCode, LoginUserDTO loginUserDTO) {
        FlowWorkNodeProcess.instansWorkNode(flowCode, loginUserDTO).deal(NodeTransferDataDTO.instanct("data"), loginUserDTO);
    }


    public void nextWorkNode(String instantsNo, LoginUserDTO loginUserDTO) {
        NodeTransferDataDTO data = NodeTransferDataDTO.instanct("data");
        data.setNext(true);
        FlowWorkNodeProcess.currentWorkFlow(instantsNo).dealBusiness(data, loginUserDTO);
    }
}
