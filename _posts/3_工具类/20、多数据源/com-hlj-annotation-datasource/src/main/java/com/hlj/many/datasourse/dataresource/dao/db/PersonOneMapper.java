package com.hlj.many.datasourse.dataresource.dao.db;

import com.hlj.many.datasourse.dataresource.dao.db.entry.PersonOne;
import org.apache.ibatis.annotations.Param;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/19  下午1:16.
 */
public interface PersonOneMapper {

    PersonOne findById(@Param("id") Long id);
}
