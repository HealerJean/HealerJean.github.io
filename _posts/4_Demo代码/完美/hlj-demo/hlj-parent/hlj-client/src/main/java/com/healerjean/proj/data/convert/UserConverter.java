package com.healerjean.proj.data.convert;

import com.healerjean.proj.common.data.bo.PageQueryBO;
import com.healerjean.proj.common.data.convert.PageCoverter;
import com.healerjean.proj.common.data.req.PageReq;
import com.healerjean.proj.data.bo.UserDemoBO;
import com.healerjean.proj.data.bo.UserDemoQueryBO;
import com.healerjean.proj.data.po.UserDemo;
import com.healerjean.proj.data.req.UserDemoQueryReq;
import com.healerjean.proj.data.req.UserDemoSaveReq;
import com.healerjean.proj.data.vo.UserDemoVO;
import com.healerjean.proj.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author zhangyujin
 * @date 2023/6/14  11:34.
 */
@Mapper
public interface UserConverter {

    /**
     * 实例
     */
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);

    /**
     * covertUserPoToBo
     *
     * @param userDemo userDemo
     * @return UserDemoBO
     */
    default UserDemoBO covertUserDemoPoToBo(UserDemo userDemo) {
        if (Objects.isNull(userDemo)) {
            return null;
        }
        UserDemoBO result = new UserDemoBO();
        result.setId(userDemo.getId());
        result.setName(userDemo.getName());
        result.setAge(userDemo.getAge());
        result.setPhone(userDemo.getPhone());
        result.setEmail(userDemo.getEmail());
        result.setStartTime(userDemo.getStartTime());
        result.setEndTime(userDemo.getEndTime());
        result.setCreateTime(userDemo.getCreateTime());
        result.setUpdateTime(userDemo.getUpdateTime());
        return result;
    }


    /**
     * covertUserPoToBo
     *
     * @param userDemo userDemo
     * @return UserDemoVO
     */
    default UserDemoVO covertUserDemoBoToVo(UserDemoBO userDemo) {
        if (Objects.isNull(userDemo)) {
            return null;
        }
        UserDemoVO result = new UserDemoVO();
        result.setId(userDemo.getId());
        result.setName(userDemo.getName());
        result.setAge(userDemo.getAge());
        result.setPhone(userDemo.getPhone());
        result.setEmail(userDemo.getEmail());
        result.setValidFlag(userDemo.getValidFlag());
        result.setStartTime(DateUtils.toDateString(userDemo.getStartTime()));
        result.setEndTime(DateUtils.toDateString(userDemo.getEndTime()));
        result.setCreateTime(DateUtils.toDateString(userDemo.getCreateTime()));
        result.setUpdateTime(DateUtils.toDateString(userDemo.getUpdateTime()));
        return result;
    }


    /**
     * covertUserDemoPoToBoList
     *
     * @param userDemos userDemos
     * @return List<UserDemoBO>
     */
    default List<UserDemoBO> covertUserDemoPoToBoList(List<UserDemo> userDemos) {
        if (CollectionUtils.isEmpty(userDemos)) {
            return Collections.emptyList();
        }
        return userDemos.stream().map(this::covertUserDemoPoToBo).collect(Collectors.toList());
    }


    /**
     * covertUserDemoPoToBoList
     *
     * @param userDemos userDemos
     * @return List<UserDemoVO>
     */
    default List<UserDemoVO> covertUserDemoBoToVoList(List<UserDemoBO> userDemos) {
        if (CollectionUtils.isEmpty(userDemos)) {
            return Collections.emptyList();
        }
        return userDemos.stream().map(this::covertUserDemoBoToVo).collect(Collectors.toList());
    }

    /**
     * covertUserDemoQueryReqToBo
     *
     * @param req req
     * @return UserDemoQueryBO
     */
    default UserDemoQueryBO covertUserDemoQueryReqToBo(UserDemoQueryReq req) {
        if (Objects.isNull(req)) {
            return null;
        }
        UserDemoQueryBO userDemoQueryBO = new UserDemoQueryBO();
        userDemoQueryBO.setId(req.getId());
        userDemoQueryBO.setName(req.getName());
        userDemoQueryBO.setAge(req.getAge());
        userDemoQueryBO.setPhone(req.getPhone());
        userDemoQueryBO.setEmail(req.getEmail());
        userDemoQueryBO.setValidFlag(req.getValidFlag());
        userDemoQueryBO.setQueryTime(DateUtils.toLocalDateTime(req.getQueryTime()));
        userDemoQueryBO.setLikeName(req.getLikeName());
        userDemoQueryBO.setLikePhone(req.getLikePhone());
        userDemoQueryBO.setSelectFields(req.getSelectFields());
        userDemoQueryBO.setOrderByList(PageCoverter.INSTANCE.coverOrderByDtoToBoList(req.getOrderByList()));
        return userDemoQueryBO;

    }


    /**
     * covertUserDemoQueryReqToBo
     *
     * @param req req
     * @return UserDemoQueryBO
     */
    default UserDemoBO covertUserDemoSaveReqToBo(UserDemoSaveReq req) {
        if (Objects.isNull(req)) {
            return null;
        }
        UserDemoBO userDemoBO = new UserDemoBO();
        userDemoBO.setName(req.getName());
        userDemoBO.setAge(req.getAge());
        userDemoBO.setPhone(req.getPhone());
        userDemoBO.setEmail(req.getEmail());
        userDemoBO.setStartTime(DateUtils.toLocalDateTime(req.getStartTime()));
        userDemoBO.setEndTime(DateUtils.toLocalDateTime(req.getEndTime()));
        return userDemoBO;

    }


    /**
     * covertUserDemoQueryPageReqToBo
     *
     * @param req req
     * @return UserDemoQueryBO
     */
    default PageQueryBO<UserDemoQueryBO> covertUserDemoQueryPageReqToBo(PageReq<UserDemoQueryReq> req) {
        if (Objects.isNull(req)) {
            return null;
        }
        UserDemoQueryBO result = Optional.ofNullable(covertUserDemoQueryReqToBo(req.getData())).orElse(new UserDemoQueryBO());
        PageQueryBO<UserDemoQueryBO> pageQuery = PageCoverter.INSTANCE.pageQueryDtoToBo(req.getPageQuery());
        pageQuery.setData(result);
        return pageQuery;
    }

    /**
     * covertUserDemoBoToPo
     *
     * @param userDemoBo userDemoBo
     * @return UserDemo
     */
    default UserDemo covertUserDemoBoToPo(UserDemoBO userDemoBo) {
        UserDemo userDemo = new UserDemo();
        userDemo.setId(userDemoBo.getId());
        userDemo.setName(userDemoBo.getName());
        userDemo.setAge(userDemoBo.getAge());
        userDemo.setPhone(userDemoBo.getPhone());
        userDemo.setEmail(userDemoBo.getEmail());
        userDemo.setValidFlag(userDemoBo.getValidFlag());
        userDemo.setStartTime(userDemoBo.getStartTime());
        userDemo.setEndTime(userDemoBo.getEndTime());
        userDemo.setCreateTime(userDemoBo.getCreateTime());
        userDemo.setUpdateTime(userDemoBo.getUpdateTime());
        return userDemo;

    }
}
