package com.healerjean.proj.template.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.healerjean.proj.template.po.FileTask;
import com.healerjean.proj.template.dao.FileTaskDao;
import com.healerjean.proj.template.mapper.FileTaskMapper;

/**
 * 文件任务(FileTask)Dao实现类
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
@Service
public class FileTaskDaoImpl extends ServiceImpl<FileTaskMapper, FileTask> implements FileTaskDao {

}

