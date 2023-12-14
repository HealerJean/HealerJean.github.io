package com.healerjean.proj.service.filetask;

import com.alibaba.fastjson.JSON;
import com.healerjean.proj.data.mq.FileTaskMq;
import com.healerjean.proj.enums.FileTaskEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * FileTaskListener
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@Slf4j
@Component
public class FileTaskListener {

    /**
     * fileTaskHandlers
     */
    @Resource
    private List<FileTaskHandler> fileTaskHandlers;

    /**
     * execute
     *
     * @param fileTaskMq 对象
     */
    public void execute(FileTaskMq fileTaskMq) {
        FileTaskEnum.BusinessTypeEnum businessTypeEnum = FileTaskEnum.BusinessTypeEnum.toBusinessTypeEnum(fileTaskMq.getBusinessType());
        if (Objects.isNull(businessTypeEnum)) {
            log.error("[FileTaskListener#execute] 非法 businessTypeEnumfileTaskMq:{}", JSON.toJSONString(fileTaskMq));
            return;
        }

        for (FileTaskHandler fileTaskHandler : fileTaskHandlers) {
            boolean match = fileTaskHandler.match(fileTaskMq.getBusinessType());
            if (Boolean.TRUE.equals(match)) {
                try {
                    fileTaskHandler.handler(fileTaskMq.getTaskId());
                } catch (Exception e) {
                    log.error("任务执行异常, fileTaskMq:{}", JSON.toJSONString(fileTaskMq), e);
                    throw new RuntimeException("任务执行异常");
                }
            }
        }
    }
}
