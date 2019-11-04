package com.hlj.many.datasourse.dataresource.dao.db;

import com.hlj.many.datasourse.dataresource.dao.db.entry.PersonTwo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/4/19  下午1:16.
 */
public interface PersonTwoMapper {

    PersonTwo findById(@Param("id") Long id);

    List<PersonTwo> findALL();
}
