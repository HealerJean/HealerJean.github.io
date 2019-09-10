package com.hlj.proj.api.point;

import com.hlj.proj.dto.Point.PointDTO;

/**
 * @author HealerJean
 * @ClassName PointService
 * @date 2019/9/9  15:09.
 * @Description
 */
public interface PointService {

    /**
     * 给用户添加积分
     */
    void addPoint(PointDTO pointDTO);

}
