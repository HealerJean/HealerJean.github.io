package com.healerjean.proj.controller;

import com.healerjean.proj.common.anno.LogIndex;
import com.healerjean.proj.common.data.bo.BaseRes;
import com.healerjean.proj.data.bo.FileTaskBO;
import com.healerjean.proj.data.bo.FileTaskQueryBO;
import com.healerjean.proj.data.converter.FileTaskConverter;
import com.healerjean.proj.data.mq.FileTaskMq;
import com.healerjean.proj.data.req.FileTaskQueryReq;
import com.healerjean.proj.data.req.FileTaskSaveReq;
import com.healerjean.proj.data.vo.FileTaskVO;
import com.healerjean.proj.service.FileTaskService;
import com.healerjean.proj.service.filetask.FileTaskListener;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * FileTaskController
 *
 * @author zhangyujin
 * @date 2023/12/13
 */
@RestController
@RequestMapping("hlj/fileTask")
@Api(tags = "FileTaskController")
@Slf4j
public class FileTaskController {

    /**
     * fileTaskService
     */
    @Resource
    private FileTaskService fileTaskService;

    /**
     * fileTaskListener
     */
    @Resource
    private FileTaskListener fileTaskListener;

    /**
     * 保存-FileTask
     *
     * @param req req
     * @return boolean
     */
    @ApiOperation("新增任务")
    @LogIndex
    @PostMapping("save")
    @ResponseBody
    public BaseRes<Boolean> saveFileTask(@RequestBody FileTaskSaveReq req) {
        FileTaskBO bo = FileTaskConverter.INSTANCE.convertFileTaskSaveReqToBo(req);
        bo.setTaskId("T" + System.currentTimeMillis());
        return BaseRes.buildSuccess(fileTaskService.saveFileTask(bo));
    }


    /**
     * 更新-FileTask
     *
     * @param req req
     * @return boolean
     */
    @ApiOperation("修改任务")
    @LogIndex
    @PutMapping("{taskId}")
    @ResponseBody
    public BaseRes<Boolean> updateFileTask(@NotNull @PathVariable String taskId, @RequestBody FileTaskSaveReq req) {
        FileTaskBO bo = FileTaskConverter.INSTANCE.convertFileTaskSaveReqToBo(req);
        bo.setTaskId(taskId);
        return BaseRes.buildSuccess(fileTaskService.updateFileTask(bo));
    }

    /**
     * 单条查询-FileTask
     *
     * @param req req
     * @return FileTaskDTO
     */
    @LogIndex
    @ApiOperation("单条查询任务")
    @GetMapping("querySingle")
    @ResponseBody
    public BaseRes<FileTaskVO> queryFileTaskSingle(FileTaskQueryReq req) {
        FileTaskQueryBO queryBo = FileTaskConverter.INSTANCE.convertFileTaskQueryReqToBo(req);
        FileTaskBO bo = fileTaskService.queryFileTaskSingle(queryBo);
        return BaseRes.buildSuccess(FileTaskConverter.INSTANCE.convertFileTaskBoToVo(bo));
    }

    /**
     * 列表查询-FileTask
     *
     * @param req req
     * @return List<FileTaskDTO>
     */
    @ApiOperation("列表查询任务")
    @LogIndex
    @GetMapping("list")
    @ResponseBody
    public BaseRes<List<FileTaskVO>> queryFileTaskList(FileTaskQueryReq req) {
        FileTaskQueryBO queryBo = FileTaskConverter.INSTANCE.convertFileTaskQueryReqToBo(req);
        List<FileTaskBO> bos = fileTaskService.queryFileTaskList(queryBo);
        return BaseRes.buildSuccess(FileTaskConverter.INSTANCE.convertFileTaskBoToVoList(bos));
    }


    /**
     * 列表查询-FileTask
     *
     * @param fileTaskMq fileTaskMq
     * @return List<FileTaskDTO>
     */
    @ApiOperation("用户信息-列表查询")
    @LogIndex
    @GetMapping("executeTask")
    @ResponseBody
    public BaseRes<Boolean> executeTask(FileTaskMq fileTaskMq) {
        fileTaskListener.execute(fileTaskMq);
        return BaseRes.buildSuccess(true);
    }

}
