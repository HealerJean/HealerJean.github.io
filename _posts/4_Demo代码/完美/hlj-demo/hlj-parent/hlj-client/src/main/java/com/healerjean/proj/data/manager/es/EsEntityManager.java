package com.healerjean.proj.data.manager.es;

import com.healerjean.proj.data.bo.EsBaseBO;
import com.healerjean.proj.data.bo.EsBaseQueryBO;
import com.healerjean.proj.data.po.EsBasePO;

import java.util.List;

/**
 * EsManager
 *
 * @author zhangyujin
 * @date 2024/3/22
 */
public interface EsEntityManager<P extends EsBasePO, B extends EsBaseBO, EQ extends EsBaseQueryBO<B>> {


    /**
     * 创建文档
     *
     * @param po po
     * @return {@link boolean}
     */
    boolean saveDocument(P po);

    /**
     * 批量插入文档
     * @param pos pos
     */
    void batchInsertDocument(List<P> pos);

    /**
     * 删除文档
     *
     * @param idList idList
     */
    void deleteDocument(List<String> idList);

    /**
     * 根据条件批量删除
     *
     * @param eq eq
     */
    void batchDeleteDocument(EQ eq);


}
