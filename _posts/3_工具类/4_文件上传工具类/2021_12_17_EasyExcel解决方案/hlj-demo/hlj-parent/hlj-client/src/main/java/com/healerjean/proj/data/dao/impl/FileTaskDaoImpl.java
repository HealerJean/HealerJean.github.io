package com.healerjean.proj.data.dao.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.data.dao.FileTaskDao;
import com.healerjean.proj.data.mapper.FileTaskMapper;
import com.healerjean.proj.data.po.FileTask;
import org.springframework.stereotype.Service;

/**
 * 文件任务(FileTask)Dao实现类
 *
 * @author zhangyujin
 * @date 2023-12-13 15:21:29
 */
@Service
public class FileTaskDaoImpl extends ServiceImpl<FileTaskMapper, FileTask> implements FileTaskDao {

}

