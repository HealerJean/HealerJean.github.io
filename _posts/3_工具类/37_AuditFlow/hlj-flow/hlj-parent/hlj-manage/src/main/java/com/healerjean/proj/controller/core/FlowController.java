package com.healerjean.proj.controller.core;

import com.healerjean.proj.api.core.FlowService;
import com.healerjean.proj.common.group.ValidateGroup;
import com.healerjean.proj.constant.CommonConstants;
import com.healerjean.proj.controller.BaseController;
import com.healerjean.proj.dto.ResponseBean;
import com.healerjean.proj.dto.flow.AuditDefaultConfigDTO;
import com.healerjean.proj.dto.flow.AuditTaskDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.util.UserUtils;
import com.healerjean.proj.utils.validate.ValidateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @author HealerJean
 * @ClassName FlowController
 * @date 2019-11-03  18:49.
 * @Description
 */
@RestController
@RequestMapping("hlj/flow")
@Api(description = "流程管理")
@Slf4j
public class FlowController extends BaseController {

    @Autowired
    private FlowService flowService;

    @ApiOperation(value = "审批配置默认审批人",
            notes = "审批配置默认审批人",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @PostMapping(value = "audit/configDefultAuditUser", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean configDefultAuditUser(@RequestBody AuditDefaultConfigDTO auditDefaultConfigDTO) {
        log.info("流程管理---------审批配置默认审批人---------请求参数：{}", auditDefaultConfigDTO);
        String validate = ValidateUtils.validate(auditDefaultConfigDTO, ValidateGroup.ConfigDefultAuditUser.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            throw new ParameterErrorException(validate);
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        flowService.configDefultAuditUser(auditDefaultConfigDTO, loginUserDTO);
        return ResponseBean.buildSuccess("审批配置默认审批人成功");
    }


    @ApiOperation(value = "发起审批申请",
            notes = "发起审批申请",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @PostMapping(value = "audit/taskApply", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean taskAuditApply(@RequestBody AuditTaskDTO auditTaskDTO) {
        log.info("流程管理---------发起审批申请---------请求参数：{}", auditTaskDTO);
        String validate = ValidateUtils.validate(auditTaskDTO, ValidateGroup.TaskAuditApply.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            throw new ParameterErrorException(validate);
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        flowService.taskAuditApply(auditTaskDTO, loginUserDTO);
        return ResponseBean.buildSuccess("发起审批申请成功");
    }


    @ApiOperation(value = "任务审批",
            notes = "任务审批",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @PostMapping(value = "audit", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean auditSuccess(@RequestBody AuditTaskDTO auditTaskDTO) {
        log.info("流程管理---------任务审批---------请求参数：{}", auditTaskDTO);
        String validate = ValidateUtils.validate(auditTaskDTO, ValidateGroup.AuditTask.class);
        if (!CommonConstants.COMMON_SUCCESS.equals(validate)) {
            throw new ParameterErrorException(validate);
        }
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        if (auditTaskDTO.getAuditResult()) {
            flowService.auditSuccess(auditTaskDTO, loginUserDTO);
        } else {
            flowService.auditReject(auditTaskDTO, loginUserDTO);
        }
        return ResponseBean.buildSuccess("任务审批成功");
    }



    @ApiOperation(value = "查询代处理审批任务",
            notes = "查询代处理审批任务",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            response = LoginUserDTO.class
    )
    @GetMapping(value = "todoTask", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBean todoTask() {
        log.info("流程管理---------查询代处理审批任务");
        LoginUserDTO loginUserDTO = UserUtils.getLoginUser();
        flowService.todoTaskSum();
        return ResponseBean.buildSuccess("查询代办审批任务");
    }






}
