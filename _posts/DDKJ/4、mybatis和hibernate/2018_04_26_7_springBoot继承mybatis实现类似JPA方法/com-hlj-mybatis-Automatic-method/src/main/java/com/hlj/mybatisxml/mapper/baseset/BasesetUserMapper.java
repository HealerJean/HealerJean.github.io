package com.hlj.mybatisxml.mapper.baseset;


import com.hlj.mybatisxml.entity.baseset.BasesetUser;
import com.hlj.mybatisxml.utility.BaseMapper;

import java.util.List;

public interface BasesetUserMapper extends BaseMapper<BasesetUser> {

     List<BasesetUser> findMyall();
}