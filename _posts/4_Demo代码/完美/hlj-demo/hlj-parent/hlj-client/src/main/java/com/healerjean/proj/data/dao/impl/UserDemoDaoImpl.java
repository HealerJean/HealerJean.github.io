package com.healerjean.proj.data.dao.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healerjean.proj.data.dao.UserDemoDao;
import com.healerjean.proj.data.mapper.UserDemoMapper;
import com.healerjean.proj.data.po.UserDemo;
import org.apache.ibatis.binding.MapperMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * UserDemoDaoImpl
 *
 * @author zhangyujin
 * @date 2023/6/14  15:33.
 */
@Service
public class UserDemoDaoImpl extends ServiceImpl<UserDemoMapper, UserDemo> implements UserDemoDao {


    /**
     * 根据唯一索引插入或更新
     *
     * @param userDemos userDemos
     * @return {@link boolean}
     */
    @Override
    public boolean saveOrUpdateBatchByUniqueKey(List<UserDemo> userDemos) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(this.entityClass);
        return this.executeBatch(userDemos, 1000, ((sqlSession, userDemo) -> {
            LambdaUpdateWrapper<UserDemo> queryWrapper = new LambdaUpdateWrapper<>();
            queryWrapper.eq(UserDemo::getEmail, userDemo.getEmail());
            UserDemo dbPo = this.getOne(queryWrapper);
            if (Objects.nonNull(dbPo)) {
                MapperMethod.ParamMap<Object> param = new MapperMethod.ParamMap<>();

                LambdaUpdateWrapper<UserDemo> updateWrapper = new LambdaUpdateWrapper<>();
                updateWrapper.set(UserDemo::getAge, userDemo.getAge())
                        .eq(UserDemo::getEmail, userDemo.getEmail());
                UserDemo updatePo = new UserDemo();
                // if (Objects.nonNull(updatePo.getAge())){
                //     updatePo.setAge(userDemo.getAge());
                // }
                param.put(Constants.WRAPPER, updateWrapper);
                param.put(Constants.ENTITY, null);
                sqlSession.update(tableInfo.getSqlStatement(SqlMethod.UPDATE.getMethod()), param);
            } else {
                sqlSession.insert(tableInfo.getSqlStatement(SqlMethod.INSERT_ONE.getMethod()), userDemo);
            }
        }));
    }
}
