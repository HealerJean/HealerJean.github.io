package com.hlj.proj.controller.flow;

import com.hlj.proj.common.ValidateGroup;
import com.hlj.proj.constants.CommonConstants;
import com.hlj.proj.dto.PageDTO;
import com.hlj.proj.dto.ResponseBean;
import com.hlj.proj.dto.user.IdentityInfoDTO;
import com.hlj.proj.exception.BusinessException;
import com.hlj.proj.service.flow.service.dto.AuditJobCollectDTO;
import com.hlj.proj.service.flow.service.dto.AuditRecordDTO;
import com.hlj.proj.service.flow.service.dto.AuditorResultDTO;
import com.hlj.proj.service.flow.service.ScfFlowService;
import com.hlj.proj.util.UserUtils;
import com.hlj.proj.utils.validate.ValidateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Slf4j
@RequestMapping("api/flow")
public class FlowController  {

    @Autowired
    private ScfFlowService scfFlowService;

    /**
     * 查询当前待办任务
     *
     * @return
     */
    @GetMapping("todo")
    public ResponseBean todoList() {
        IdentityInfoDTO authUser = UserUtils.getAuthUser();
        List<AuditJobCollectDTO> auditJobCollectDTOS = scfFlowService.jobCollect(authUser);
        log.info("查询当前待办任务成功，{}", auditJobCollectDTOS);
        return ResponseBean.buildSuccess("查询当前待办任务成功", auditJobCollectDTOS);
    }


    /**
     * 查询任务列表
     */
    @GetMapping("audits")
    public ResponseBean audits(AuditJobCollectDTO auditRecordDTO) {
        IdentityInfoDTO authUser = UserUtils.getAuthUser();
        PageDTO<AuditRecordDTO> auditRecordDTOPageDTO = scfFlowService.readyAudits(auditRecordDTO, authUser);
        log.info("查询任务列表成功，{}", auditRecordDTOPageDTO);
        return ResponseBean.buildSuccess("查询任务列表成功", auditRecordDTOPageDTO);
    }


    /**
     * 审批
     * @return
     */
    @PostMapping("audit")
    public ResponseBean audits(@RequestBody AuditorResultDTO auditorResultDTO) {
        if (auditorResultDTO == null) {
            throw new BusinessException("审批参数不全");
        }
        String validate = ValidateUtils.validate(auditorResultDTO, ValidateGroup.Audit.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            log.error("审批-------审批-----参数不完整{}", validate);
            throw new BusinessException(validate);
        }
        IdentityInfoDTO authUser = UserUtils.getAuthUser();
        scfFlowService.audit(auditorResultDTO, authUser);
        return  ResponseBean.buildSuccess();
    }
}
