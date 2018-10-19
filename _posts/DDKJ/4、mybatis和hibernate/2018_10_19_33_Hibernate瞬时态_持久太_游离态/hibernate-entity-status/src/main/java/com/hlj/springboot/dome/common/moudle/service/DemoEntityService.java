package com.hlj.springboot.dome.common.moudle.service;

/**
 * @Desc:
 * @Author HealerJean
 * @Date 2018/9/17  下午2:39.
 */
public interface DemoEntityService {

    void testNoHaveSaveButSaveSuccess();


    void persist();
    void persistHaveId();

    void merge();
    void mergeHaveId();

    void remove();

    void refresh();


}
