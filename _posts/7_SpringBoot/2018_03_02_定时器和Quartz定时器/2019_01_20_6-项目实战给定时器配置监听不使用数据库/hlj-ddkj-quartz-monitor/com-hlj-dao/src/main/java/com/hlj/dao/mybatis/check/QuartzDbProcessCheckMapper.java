package com.hlj.dao.mybatis.check;

import com.hlj.data.res.check.JobDetailData;
import com.hlj.data.res.check.QuartzCheckData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 类名称：QuartzDbProcessCheckMapper
 * 类描述：job启动检查任务
 * 创建人：HealerJean
 * 修改人：
 * 修改备注：
 *
 * @version 1.0.0
 */
public interface QuartzDbProcessCheckMapper {

    /**
     * 查看本应该在 现在的时间之前开始的任务
     */
     List<QuartzCheckData> findJobList(@Param("next_fire_time") Long time);

    /**
     * 根据工作名 查找工作详情
     * @param jobName
     * @return
     */
     JobDetailData findJobDetailData(@Param("job_name") String jobName);

}
