package com.hlj.proj.service.point;

import com.hlj.proj.api.point.PointService;
import com.hlj.proj.data.dao.mybatis.manager.point.TpointManager;
import com.hlj.proj.data.pojo.user.Tpoint;
import com.hlj.proj.dto.Point.PointDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HealerJean
 * @ClassName PointServiceImpl
 * @date 2019/9/9  15:22.
 * @Description
 */
@Service
@Slf4j
public class PointServiceImpl implements PointService {

    @Autowired
    private TpointManager tpointManager;

    /**
     * 给用户添加积分
     *
     * @param pointDTO
     */
    @Override
    public void addPoint(PointDTO pointDTO) {
        Tpoint tpoint = new Tpoint();
        tpoint.setUserId(pointDTO.getUserId());
        tpoint.setAmount(pointDTO.getPointAmount());
        tpointManager.insertSelective(tpoint);

    }
}
