package com.hlj.proj.data.dao.mybatis.demo;

import com.hlj.proj.data.pojo.demo.DemoEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 作者 ：HealerJean
 * 日期 ：2018/11/14  下午12:54.
 * 类描述：
 */
public interface DemoEntityMapper {

    List<DemoEntity> findAll();

    DemoEntity findById(@Param("id") Long id);


}
