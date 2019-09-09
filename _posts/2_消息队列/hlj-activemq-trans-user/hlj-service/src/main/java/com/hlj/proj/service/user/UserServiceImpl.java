package com.hlj.proj.service.user;

import com.hlj.proj.api.user.UserEventService;
import com.hlj.proj.api.user.UserService;
import com.hlj.proj.data.dao.mybatis.dao.user.TuserDao;
import com.hlj.proj.data.pojo.user.Tuser;
import com.hlj.proj.dto.user.EventDTO;
import com.hlj.proj.dto.user.PointDTO;
import com.hlj.proj.dto.user.UserDTO;
import com.hlj.proj.enums.BusinessEnum;
import com.hlj.proj.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName UserServiceImpl
 * @date 2019/9/9  14:19.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TuserDao tuserDao;

    @Autowired
    private UserEventService userEventService;


    /**
     * 添加用户
     * 1、添加用户
     * 2、添加事件
     */
    @Override
    public UserDTO addUser(UserDTO userDTO) {

        // 1、添加用户
        Tuser tuser = new Tuser();
        tuser.setUserName(userDTO.getName());
        tuserDao.insertSelective(tuser);

        // 2、添加事件
        EventDTO eventDTO = new EventDTO();
        eventDTO.setType(BusinessEnum.EventType.新增用户.code);
        eventDTO.setProcess(BusinessEnum.EventProcess.新建.code);
        PointDTO pointDTO = new PointDTO();
        pointDTO.setUserId(tuser.getId());
        pointDTO.setPointAmount(userDTO.getPointAmount());
        eventDTO.setContent(JsonUtils.toJsonString(pointDTO));
        userEventService.addEvent(eventDTO);

        userDTO.setUserId(tuser.getId());
        return userDTO;
    }
}
