package com.hlj.moudle.design.D06访问数据结构.D13Visitor访问模式;

/**
 * @author HealerJean
 * @ClassName BaseComputerPart
 * @date 2019/8/14  20:24.
 * @Description
 */

/**
 * 电脑的零配件的父抽象类
 *
 * @author wangXgnaw
 */
public abstract class BaseComputerPart {
    /**
     * 所有的 零配件，都必须通过一个硬件接口进行连接
     */
    protected abstract void link(HardwareInter hardwareInter);

}
