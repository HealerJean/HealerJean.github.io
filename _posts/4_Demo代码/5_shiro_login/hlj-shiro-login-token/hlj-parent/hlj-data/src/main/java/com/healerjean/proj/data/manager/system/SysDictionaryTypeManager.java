package com.healerjean.proj.data.manager.system;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysDictionaryTypeDao;
import com.healerjean.proj.data.pojo.system.SysDictionaryType;
import com.healerjean.proj.data.pojo.system.SysDictionaryTypePage;
import com.healerjean.proj.data.pojo.system.SysDictionaryTypeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryTypeManager
 * @date 2099/1/1
 * @Description: SysDictionaryTypeManager
 */
@Component("sysDictionaryTypeManager")
public class SysDictionaryTypeManager {

    @Autowired
    @Qualifier("sysDictionaryTypeDao")
    private SysDictionaryTypeDao sysDictionaryTypeDao;

    public SysDictionaryTypeDao getDao() {
        return this.sysDictionaryTypeDao;
    }

    public int save(SysDictionaryType sysDictionaryType) {
        int cnt = 0;
        if (sysDictionaryType.getId() == null) {
            cnt = sysDictionaryTypeDao.insertSelective(sysDictionaryType);
        } else {
            cnt = sysDictionaryTypeDao.updateByPrimaryKeySelective(sysDictionaryType);
        }
        return cnt;
    }

    public int update(SysDictionaryType sysDictionaryType) {
        return sysDictionaryTypeDao.updateByPrimaryKey(sysDictionaryType);
    }

    public int updateSelective(SysDictionaryType sysDictionaryType) {
        return sysDictionaryTypeDao.updateByPrimaryKeySelective(sysDictionaryType);
    }

    public int insert(SysDictionaryType sysDictionaryType) {
        return sysDictionaryTypeDao.insert(sysDictionaryType);
    }

    public int insertSelective(SysDictionaryType sysDictionaryType) {
        return sysDictionaryTypeDao.insertSelective(sysDictionaryType);
    }

    public int batchInsert(List<SysDictionaryType> list) {
        return sysDictionaryTypeDao.batchInsert(list);
    }

    public SysDictionaryType findById(long id) {
        return sysDictionaryTypeDao.selectByPrimaryKey(id);
    }

    public SysDictionaryType findByQueryContion(SysDictionaryTypeQuery query) {
        List<SysDictionaryType> list = sysDictionaryTypeDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysDictionaryType> queryList(SysDictionaryTypeQuery query) {
        return sysDictionaryTypeDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysDictionaryTypeDao.deleteByPrimaryKey(id);
    }

    public int delete(SysDictionaryTypeQuery query) {
        return sysDictionaryTypeDao.deleteByExample(query);
    }

    public SysDictionaryTypePage queryPageList(SysDictionaryTypeQuery query) {
        SysDictionaryTypePage sysDictionaryTypePage = new SysDictionaryTypePage();
        Integer itemCount = sysDictionaryTypeDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysDictionaryTypePage.setValues(null);
        } else {
            sysDictionaryTypePage.setValues(sysDictionaryTypeDao.selectPageByExample(query));
        }

        sysDictionaryTypePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysDictionaryTypePage;
    }


    public SysDictionaryTypePage queryDictTypePageLikes(SysDictionaryTypeQuery query) {
        SysDictionaryTypePage typePage = new SysDictionaryTypePage();
        Integer itemCount = sysDictionaryTypeDao.countDictTypeLikes(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            typePage.setValues(null);
        } else {
            typePage.setValues(sysDictionaryTypeDao.queryDictTypePageLikes(query));
        }
        typePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return typePage;
    }

    public List<SysDictionaryType> queryDictTypeLikes(SysDictionaryTypeQuery query) {
        return sysDictionaryTypeDao.queryDictTypeLikes(query);
    }


}
