package com.healerjean.proj.service.core;

import com.healerjean.proj.api.core.AuditService;
import com.healerjean.proj.constant.DictionaryConstants;
import com.healerjean.proj.data.manager.flow.*;
import com.healerjean.proj.data.manager.system.SysDictionaryDataManager;
import com.healerjean.proj.data.manager.system.SysRoleManager;
import com.healerjean.proj.data.manager.system.SysUserInfoManager;
import com.healerjean.proj.data.manager.system.SysUserRoleRefManager;
import com.healerjean.proj.data.pojo.flow.*;
import com.healerjean.proj.data.pojo.system.*;
import com.healerjean.proj.dto.flow.AuditDefaultConfigDTO;
import com.healerjean.proj.dto.flow.AuditTaskDTO;
import com.healerjean.proj.dto.flow.AuditUserDTO;
import com.healerjean.proj.dto.user.LoginUserDTO;
import com.healerjean.proj.enums.FlowEnum;
import com.healerjean.proj.enums.ResponseEnum;
import com.healerjean.proj.enums.StatusEnum;
import com.healerjean.proj.exception.BusinessException;
import com.healerjean.proj.exception.ParameterErrorException;
import com.healerjean.proj.utils.EmptyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName AuditServiceImpl
 * @date 2019-11-03  16:37.
 * @Description
 */
@Slf4j
@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private FlowAuditDefaultConfigManager flowAuditDefaultConfigManager;
    @Autowired
    private FlowAuditDefaultUserManager flowAuditDefaultUserManager;
    @Autowired
    private SysUserInfoManager sysUserInfoManager;
    @Autowired
    private SysRoleManager sysRoleManager;
    @Autowired
    private FlowAuditUserDetailManager flowAuditUserDetailManager;
    @Autowired
    private FlowAuditTaskManager flowAuditTaskManager;
    @Autowired
    private FlowAuditRecordManager flowAuditRecordManager;
    @Autowired
    private SysUserRoleRefManager sysUserRoleRefManager;
    @Autowired
    private SysDictionaryDataManager sysDictionaryDataManager;

    /**
     * 配置默认审批人
     * 1、判断审批人和用户是否真实存在
     * 2、配置默认属性配置
     * 3、将之前的默认配置设置为失效
     * 4、配置新的审批人
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void configDefultAuditUser(AuditDefaultConfigDTO auditDefaultConfigDTO, LoginUserDTO loginUserDTO) {

        // 1、判断审批人和用户是否真实存在
        List<AuditUserDTO> approvers = auditDefaultConfigDTO.getApprovers();
        checkAuditUserExist(approvers);
        List<AuditUserDTO> copyUsers = auditDefaultConfigDTO.getCopyUsers();
        if (!EmptyUtils.isEmpty(copyUsers)) {
            checkAuditUserExist(copyUsers);
        }

        SysDictionaryDataQuery dictionaryDataQuery = new SysDictionaryDataQuery();
        dictionaryDataQuery.setStatus(StatusEnum.生效.code);
        dictionaryDataQuery.setRefTypeKey(DictionaryConstants.AUDIT_TASK);
        dictionaryDataQuery.setDataKey(auditDefaultConfigDTO.getTaskType());
        SysDictionaryData dictionaryData = sysDictionaryDataManager.findByQueryContion(dictionaryDataQuery);


        // 2、配置默认属性配置
        FlowAuditDefaultConfigQuery flowAuditDefaultConfigQuery = new FlowAuditDefaultConfigQuery();
        flowAuditDefaultConfigQuery.setTaskType(auditDefaultConfigDTO.getTaskType());
        flowAuditDefaultConfigQuery.setStatus(StatusEnum.生效.code);
        FlowAuditDefaultConfig auditDefaultConfig = flowAuditDefaultConfigManager.findByQueryContion(flowAuditDefaultConfigQuery);

        if (auditDefaultConfig == null) {
            auditDefaultConfig = new FlowAuditDefaultConfig();
            auditDefaultConfig.setTaskType(auditDefaultConfigDTO.getTaskType());
            auditDefaultConfig.setCreateUser(loginUserDTO.getUserId());
            auditDefaultConfig.setCreateName(loginUserDTO.getRealName());
        }
        auditDefaultConfig.setTaskName(dictionaryData.getDataValue());
        auditDefaultConfig.setModify(auditDefaultConfigDTO.getModify());
        auditDefaultConfig.setStatus(StatusEnum.生效.code);
        auditDefaultConfig.setUpdateUser(loginUserDTO.getUserId());
        auditDefaultConfig.setUpdateName(loginUserDTO.getRealName());
        flowAuditDefaultConfigManager.save(auditDefaultConfig);


        // 3、将之前的默认配置设置为失效
        FlowAuditDefaultUserQuery flowAuditDefaultUserQuery = new FlowAuditDefaultUserQuery();
        flowAuditDefaultUserQuery.setTaskType(auditDefaultConfig.getTaskType());
        flowAuditDefaultUserQuery.setStatus(StatusEnum.生效.code);
        List<FlowAuditDefaultUser> flowAuditDefaultUsers = flowAuditDefaultUserManager.queryList(flowAuditDefaultUserQuery);
        if (!EmptyUtils.isEmpty(flowAuditDefaultUsers)) {
            flowAuditDefaultUsers.stream().forEach(item -> {
                item.setStatus(StatusEnum.废弃.code);
                item.setUpdateUser(loginUserDTO.getUserId());
                item.setUpdateName(loginUserDTO.getRealName());
                flowAuditDefaultUserManager.updateSelective(item);
            });
        }


        // 4、配置新的审批人
        approvers.stream().forEach(approver -> {
            FlowAuditDefaultUser flowAuditDefaultUser = new FlowAuditDefaultUser();
            flowAuditDefaultUser.setTaskType(auditDefaultConfigDTO.getTaskType());
            flowAuditDefaultUser.setTaskName(dictionaryData.getDataValue());
            flowAuditDefaultUser.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
            flowAuditDefaultUser.setAuditObjectType(approver.getAuditObjectType());
            flowAuditDefaultUser.setAuditObjectId(approver.getAuditObjectId());
            flowAuditDefaultUser.setStep(approver.getStep());
            flowAuditDefaultUser.setStatus(StatusEnum.生效.code);
            flowAuditDefaultUser.setCreateUser(loginUserDTO.getUserId());
            flowAuditDefaultUser.setCreateName(loginUserDTO.getRealName());
            flowAuditDefaultUser.setUpdateUser(loginUserDTO.getUserId());
            flowAuditDefaultUser.setUpdateName(loginUserDTO.getRealName());
            flowAuditDefaultUserManager.insertSelective(flowAuditDefaultUser);
        });


        if (!EmptyUtils.isEmpty(copyUsers)) {
            copyUsers.stream().forEach(copyUser -> {
                FlowAuditDefaultUser flowAuditDefaultUser = new FlowAuditDefaultUser();
                flowAuditDefaultUser.setTaskType(auditDefaultConfigDTO.getTaskType());
                flowAuditDefaultUser.setTaskName(dictionaryData.getDataValue());
                flowAuditDefaultUser.setAuditUserType(FlowEnum.AuditUserTypeEnum.抄送人.code);
                flowAuditDefaultUser.setAuditObjectType(copyUser.getAuditObjectType());
                flowAuditDefaultUser.setAuditObjectId(copyUser.getAuditObjectId());
                flowAuditDefaultUser.setStep(0);
                flowAuditDefaultUser.setStatus(StatusEnum.生效.code);
                flowAuditDefaultUser.setCreateUser(loginUserDTO.getUserId());
                flowAuditDefaultUser.setCreateName(loginUserDTO.getRealName());
                flowAuditDefaultUser.setUpdateUser(loginUserDTO.getUserId());
                flowAuditDefaultUser.setUpdateName(loginUserDTO.getRealName());
                flowAuditDefaultUserManager.insertSelective(flowAuditDefaultUser);
            });
        }

    }

    private void checkAuditUserExist(List<AuditUserDTO> approvers) {
        approvers.stream().forEach(approver -> {
            FlowEnum.AuditObjectTypeEnum auditObjectTypeEnum = FlowEnum.AuditObjectTypeEnum.toEnum(approver.getAuditObjectType());
            switch (auditObjectTypeEnum) {
                case 用户:
                    SysUserInfo userInfo = sysUserInfoManager.findById(approver.getAuditObjectId());
                    if (StatusEnum.废弃.code.equals(userInfo.getStatus())) {
                        throw new BusinessException(ResponseEnum.用户不存在);
                    }
                    break;
                case 角色:
                    SysRole role = sysRoleManager.findById(approver.getAuditObjectId());
                    if (StatusEnum.废弃.code.equals(role.getStatus())) {
                        throw new BusinessException(ResponseEnum.角色不存在);
                    }
                    break;
                default:
                    throw new BusinessException(ResponseEnum.枚举不存在);
            }
        });
    }


    /**
     * 发起审批申请
     * 1、判断审批人是否存在
     * 2、添加审批任务
     * 3、保存审批人任务详情
     * 4、保存抄送人任务详情
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void taskAuditApply(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO) {

        // 1、判断审批人是否存在
        FlowAuditDefaultUserQuery approverUserQuery = new FlowAuditDefaultUserQuery();
        approverUserQuery.setTaskType(auditTaskDTO.getTaskType());
        approverUserQuery.setStatus(StatusEnum.生效.code);
        approverUserQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        List<FlowAuditDefaultUser> approvers = flowAuditDefaultUserManager.queryList(approverUserQuery);
        if (EmptyUtils.isEmpty(approvers)) {
            throw new BusinessException(ResponseEnum.审批人不存在);
        }


        SysDictionaryDataQuery dictionaryDataQuery = new SysDictionaryDataQuery();
        dictionaryDataQuery.setStatus(StatusEnum.生效.code);
        dictionaryDataQuery.setRefTypeKey(DictionaryConstants.AUDIT_TASK);
        dictionaryDataQuery.setDataKey(auditTaskDTO.getTaskType());
        SysDictionaryData dictionaryData = sysDictionaryDataManager.findByQueryContion(dictionaryDataQuery);
        if(dictionaryData == null){
            throw new BusinessException(ResponseEnum.字典数据项不存在);
        }

        // 2、添加审批任务
        FlowAuditTask auditTask = new FlowAuditTask();
        auditTask.setTaskType(auditTaskDTO.getTaskType());
        auditTask.setTaskName(dictionaryData.getDataValue());
        auditTask.setTaskData(auditTaskDTO.getTaskData());
        auditTask.setStatus(FlowEnum.AuditStatusEnum.等待审批.code);
        auditTask.setCreateUser(loginUserDTO.getUserId());
        auditTask.setCreateName(loginUserDTO.getRealName());
        auditTask.setUpdateUser(loginUserDTO.getUserId());
        auditTask.setUpdateName(loginUserDTO.getRealName());
        flowAuditTaskManager.insertSelective(auditTask);


        // 3、保存审批人任务详情
        approvers.stream().forEach(approver -> {
            FlowAuditUserDetail approverAuditDetail = new FlowAuditUserDetail();
            approverAuditDetail.setRefAuditTaskId(auditTask.getId());
            approverAuditDetail.setTaskType(auditTask.getTaskType());
            approverAuditDetail.setTaskName(dictionaryData.getDataValue());
            approverAuditDetail.setStep(approver.getStep());
            approverAuditDetail.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
            approverAuditDetail.setAuditObjectType(approver.getAuditObjectType());
            approverAuditDetail.setAuditObjectId(approver.getAuditObjectId());
            approverAuditDetail.setCreateUser(loginUserDTO.getUserId());
            approverAuditDetail.setCreateName(loginUserDTO.getRealName());
            approverAuditDetail.setUpdateUser(loginUserDTO.getUserId());
            approverAuditDetail.setUpdateName(loginUserDTO.getRealName());
            if (approver.getStep().intValue() == 1) {
                approverAuditDetail.setStatus(FlowEnum.AuditStatusEnum.审批中.code);
            } else {
                approverAuditDetail.setStatus(FlowEnum.AuditStatusEnum.等待审批.code);
            }
            flowAuditUserDetailManager.insertSelective(approverAuditDetail);
        });


        // 4、保存抄送人任务详情
        FlowAuditDefaultUserQuery copyUserQuery = new FlowAuditDefaultUserQuery();
        copyUserQuery.setTaskType(auditTaskDTO.getTaskType());
        copyUserQuery.setStatus(StatusEnum.生效.code);
        copyUserQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.抄送人.code);
        List<FlowAuditDefaultUser> copyUsers = flowAuditDefaultUserManager.queryList(copyUserQuery);
        if (!EmptyUtils.isEmpty(copyUsers)) {
            copyUsers.stream().forEach(copyUser -> {
                FlowAuditUserDetail copyUserAuditDetail = new FlowAuditUserDetail();
                copyUserAuditDetail.setRefAuditTaskId(auditTask.getId());
                copyUserAuditDetail.setTaskType(auditTask.getTaskType());
                copyUserAuditDetail.setTaskName(dictionaryData.getDataValue());
                copyUserAuditDetail.setStep(copyUser.getStep());
                copyUserAuditDetail.setAuditUserType(FlowEnum.AuditUserTypeEnum.抄送人.code);
                copyUserAuditDetail.setAuditObjectType(copyUser.getAuditObjectType());
                copyUserAuditDetail.setAuditObjectId(copyUser.getAuditObjectId());
                copyUserAuditDetail.setStatus(FlowEnum.AuditStatusEnum.等待审批.code);
                copyUserAuditDetail.setCreateUser(loginUserDTO.getUserId());
                copyUserAuditDetail.setCreateName(loginUserDTO.getRealName());
                copyUserAuditDetail.setUpdateUser(loginUserDTO.getUserId());
                copyUserAuditDetail.setUpdateName(loginUserDTO.getRealName());
                flowAuditUserDetailManager.insertSelective(copyUserAuditDetail);
            });
        }


    }


    /**
     * 审批成功
     * 1、判断审批任务是否存在，审批人是否有权限审批
     * 2、审批自己的部分, 保存审批记录
     * 3、判断是否还有和自己同级别的审批人，将他们的状态跟新为他人审批
     * 4、判断后面是否还有的审批，没有则表示这个审批任务终结
     * 5、后面没有审批了，说明审批成功，则判断有无抄送人并更新抄送人状态
     * 7、执行该审批任务的成功后的业务
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void auditSuccess(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO) {

        // 1、判断审批任务是否存在，审批人是否有权限审批
        FlowAuditTask auditTask = flowAuditTaskManager.findById(auditTaskDTO.getAuditTaskId());
        Long currentUserAuditDetailId = checkeAuditPermission(auditTask, loginUserDTO);

        // 2、审批自己的部分
        FlowAuditUserDetail currentUserAuditDetail = flowAuditUserDetailManager.findById(currentUserAuditDetailId);
        currentUserAuditDetail.setStatus(FlowEnum.AuditStatusEnum.成功.code);
        currentUserAuditDetail.setUpdateUser(loginUserDTO.getUserId());
        currentUserAuditDetail.setUpdateName(loginUserDTO.getRealName());
        flowAuditUserDetailManager.updateSelective(currentUserAuditDetail);


        // 保存审批记录
        FlowAuditRecord auditRecord = new FlowAuditRecord();
        auditRecord.setRefAuditTaskId(auditTask.getId());
        auditRecord.setTaskType(auditTask.getTaskType());
        auditRecord.setTaskName(auditTask.getTaskName());
        auditRecord.setStep(currentUserAuditDetail.getStep());
        auditRecord.setAuditMessage(auditTaskDTO.getAuditMessage());
        auditRecord.setRefFileIds(auditTaskDTO.getRefFileIds());
        auditRecord.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        auditRecord.setOptUserType(currentUserAuditDetail.getAuditObjectType());
        auditRecord.setAuditObjectId(currentUserAuditDetail.getAuditObjectId());
        auditRecord.setOptUserId(loginUserDTO.getUserId());
        auditRecord.setOptUserName(loginUserDTO.getRealName());
        auditRecord.setStatus(FlowEnum.AuditStatusEnum.成功.code);
        flowAuditRecordManager.insertSelective(auditRecord);


        //3、判断是否还有和自己同级别的审批人，将他们的状态跟新为他人审批
        FlowAuditUserDetailQuery otherAuditDetailQuery = new FlowAuditUserDetailQuery();
        otherAuditDetailQuery.setRefAuditTaskId(auditTask.getId());
        otherAuditDetailQuery.setStep(currentUserAuditDetail.getStep());
        otherAuditDetailQuery.setStatus(FlowEnum.AuditStatusEnum.审批中.code);
        otherAuditDetailQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        List<FlowAuditUserDetail> otherUserAuditDetails = flowAuditUserDetailManager.queryList(otherAuditDetailQuery);
        if (!EmptyUtils.isEmpty(otherUserAuditDetails)) {
            otherUserAuditDetails.stream().forEach(otherAuditDetail -> {
                otherAuditDetail.setStatus(FlowEnum.AuditStatusEnum.他人审批.code);
                otherAuditDetail.setUpdateUser(loginUserDTO.getUserId());
                otherAuditDetail.setUpdateName(loginUserDTO.getRealName());
                flowAuditUserDetailManager.updateSelective(otherAuditDetail);
            });
        }


        // 4、判断后面是否还有的审批，有的话将他设置为审批人，没有则表示这个审批任务终结、
        FlowAuditUserDetailQuery nextUserAuditDetailQuery = new FlowAuditUserDetailQuery();
        nextUserAuditDetailQuery.setRefAuditTaskId(auditTask.getId());
        nextUserAuditDetailQuery.setStep(currentUserAuditDetail.getStep() + 1);
        nextUserAuditDetailQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        List<FlowAuditUserDetail> nextUserAuditDetails = flowAuditUserDetailManager.queryList(nextUserAuditDetailQuery);
        if (!EmptyUtils.isEmpty(nextUserAuditDetails)) {
            nextUserAuditDetails.stream().forEach(nextUserAuditDetail -> {
                nextUserAuditDetail.setStatus(FlowEnum.AuditStatusEnum.审批中.code);
                nextUserAuditDetail.setUpdateUser(loginUserDTO.getUserId());
                nextUserAuditDetail.setUpdateName(loginUserDTO.getRealName());
                flowAuditUserDetailManager.updateSelective(nextUserAuditDetail);
            });
            return;
        }

        // 5、后面没有审批了，说明审批成功，则判断有无抄送人并更新抄送人状态
        FlowAuditUserDetailQuery copyUserAudtiDetailQuery = new FlowAuditUserDetailQuery();
        copyUserAudtiDetailQuery.setRefAuditTaskId(auditTask.getId());
        copyUserAudtiDetailQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.抄送人.code);
        List<FlowAuditUserDetail> copyUserAuditDetails = flowAuditUserDetailManager.queryList(copyUserAudtiDetailQuery);
        if (!EmptyUtils.isEmpty(copyUserAuditDetails)) {
            copyUserAuditDetails.stream().forEach(copyUser -> {
                copyUser.setStatus(FlowEnum.AuditStatusEnum.成功.code);
                copyUser.setUpdateUser(loginUserDTO.getUserId());
                copyUser.setUpdateName(loginUserDTO.getRealName());
                flowAuditUserDetailManager.updateSelective(copyUser);
            });
        }

        // 6、更新审批任务的状态
        auditTask.setStatus(FlowEnum.AuditStatusEnum.成功.code);
        auditTask.setUpdateUser(loginUserDTO.getUserId());
        auditTask.setUpdateName(loginUserDTO.getRealName());
        flowAuditTaskManager.updateSelective(auditTask);

        // 7、执行该审批任务的成功后的业务
    }


    /**
     * 审批驳回
     * 1、判断审批任务是否存在，审批人是否有权限审批
     * 2、审批自己的部分
     * 3、更新审批任务的状态
     * 4、保存审批记录
     * 5、判断是否还有和自己同级别的审批人，将他们的状态跟新为他人审批
     */
    @Override
    public void auditReject(AuditTaskDTO auditTaskDTO, LoginUserDTO loginUserDTO) {

        // 1、判断审批任务是否存在，审批人是否有权限审批
        FlowAuditTask auditTask = flowAuditTaskManager.findById(auditTaskDTO.getAuditTaskId());
        Long currentUserAuditDetailId = checkeAuditPermission(auditTask, loginUserDTO);

        // 2、审批自己的部分
        FlowAuditUserDetail currentUserAuditDetail = flowAuditUserDetailManager.findById(currentUserAuditDetailId);
        currentUserAuditDetail.setStatus(FlowEnum.AuditStatusEnum.拒绝.code);
        currentUserAuditDetail.setUpdateUser(loginUserDTO.getUserId());
        currentUserAuditDetail.setUpdateName(loginUserDTO.getRealName());
        flowAuditUserDetailManager.updateSelective(currentUserAuditDetail);

        // 3、更新审批任务的状态
        auditTask.setStatus(FlowEnum.AuditStatusEnum.拒绝.code);
        auditTask.setUpdateUser(loginUserDTO.getUserId());
        auditTask.setUpdateName(loginUserDTO.getRealName());
        flowAuditTaskManager.updateSelective(auditTask);

        // 4、保存审批记录
        FlowAuditRecord auditRecord = new FlowAuditRecord();
        auditRecord.setRefAuditTaskId(auditTask.getId());
        auditRecord.setTaskType(auditTask.getTaskType());
        auditRecord.setTaskName(auditTask.getTaskName());
        auditRecord.setStep(currentUserAuditDetail.getStep());
        auditRecord.setAuditMessage(auditTaskDTO.getAuditMessage());
        auditRecord.setRefFileIds(auditTaskDTO.getRefFileIds());
        auditRecord.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        auditRecord.setOptUserType(currentUserAuditDetail.getAuditObjectType());
        auditRecord.setAuditObjectId(currentUserAuditDetail.getAuditObjectId());
        auditRecord.setOptUserId(loginUserDTO.getUserId());
        auditRecord.setOptUserName(loginUserDTO.getRealName());
        auditRecord.setStatus(FlowEnum.AuditStatusEnum.拒绝.code);
        flowAuditRecordManager.insertSelective(auditRecord);

        //5、判断是否还有和自己同级别的审批人，将他们的状态跟新为他人审批
        FlowAuditUserDetailQuery otherAuditDetailQuery = new FlowAuditUserDetailQuery();
        otherAuditDetailQuery.setRefAuditTaskId(auditTask.getId());
        otherAuditDetailQuery.setStep(currentUserAuditDetail.getStep());
        otherAuditDetailQuery.setStatus(FlowEnum.AuditStatusEnum.审批中.code);
        otherAuditDetailQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        List<FlowAuditUserDetail> otherUserAuditDetails = flowAuditUserDetailManager.queryList(otherAuditDetailQuery);
        if (!EmptyUtils.isEmpty(otherUserAuditDetails)) {
            otherUserAuditDetails.stream().forEach(otherAuditDetail -> {
                otherAuditDetail.setStatus(FlowEnum.AuditStatusEnum.他人审批.code);
                otherAuditDetail.setUpdateUser(loginUserDTO.getUserId());
                otherAuditDetail.setUpdateName(loginUserDTO.getRealName());
                flowAuditUserDetailManager.updateSelective(otherAuditDetail);
            });
        }

    }


    /**
     * 检测是否用拥有审批权限
     */
    private Long checkeAuditPermission(FlowAuditTask auditTask,LoginUserDTO loginUserDTO) {
        // 1、判断审批任务是否存在，审批人是否有权限审批
        if (auditTask == null) {
            throw new BusinessException(ResponseEnum.审批任务不存在);
        }
        if (!(FlowEnum.AuditStatusEnum.审批中.code.equals(auditTask.getStatus())
                || FlowEnum.AuditStatusEnum.等待审批.code.equals(auditTask.getStatus()))) {
            throw new ParameterErrorException("该任务已经被审批了");
        }

        FlowAuditUserDetailQuery currentUserAuditDetailQuery = new FlowAuditUserDetailQuery();
        currentUserAuditDetailQuery.setRefAuditTaskId(auditTask.getId());
        currentUserAuditDetailQuery.setAuditUserType(FlowEnum.AuditUserTypeEnum.审批人.code);
        currentUserAuditDetailQuery.setStatus(FlowEnum.AuditStatusEnum.审批中.code);
        List<FlowAuditUserDetail> currentUserAuditDetails = flowAuditUserDetailManager.queryList(currentUserAuditDetailQuery);
        if (EmptyUtils.isEmpty(currentUserAuditDetails)) {
            throw new ParameterErrorException("该审批任务不存在");
        }
        AtomicReference<Long> currentUserAuditDetailId = new AtomicReference<>(0L);
        SysUserRoleRefQuery userRoleRefQuery = new SysUserRoleRefQuery();
        userRoleRefQuery.setRefUserId(loginUserDTO.getUserId());
        List<SysRole> roles = sysUserRoleRefManager.queryListToRole(userRoleRefQuery);
        List<Long> rolsIds = roles.stream().map(SysRole::getId).collect(Collectors.toList());
        AtomicBoolean permission = new AtomicBoolean(false);
        currentUserAuditDetails.stream().forEach(item -> {
            FlowEnum.AuditObjectTypeEnum auditObjectTypeEnum = FlowEnum.AuditObjectTypeEnum.toEnum(item.getAuditObjectType());
            switch (auditObjectTypeEnum) {
                case 用户:
                    if (loginUserDTO.getUserId().equals(item.getAuditObjectId())) {
                        permission.set(true);
                        currentUserAuditDetailId.set(item.getId());
                    }
                    break;
                case 角色:
                    if (rolsIds.contains(item.getAuditObjectId())) {
                        permission.set(true);
                        currentUserAuditDetailId.set(item.getId());
                    }
                    break;
                default:
                    throw new BusinessException(ResponseEnum.枚举不存在);
            }
        });

        if (!permission.get()) {
            throw new ParameterErrorException("您没有该任务的审批权限");
        }

        return currentUserAuditDetailId.get();
    }





}
