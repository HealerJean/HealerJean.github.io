package com.healerjean.proj.data.manager.system;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.pojo.system.SysUserInfo;
import com.healerjean.proj.data.pojo.system.SysUserInfoPage;
import com.healerjean.proj.data.pojo.system.SysUserInfoQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysUserInfoManager
 * @date 2099/1/1
 * @Description: SysUserInfoManager
 */
@Component("sysUserInfoManager")
public class SysUserInfoManager {

    @Autowired
    @Qualifier("sysUserInfoDao")
    private com.healerjean.proj.data.dao.system.SysUserInfoDao sysUserInfoDao;

    public com.healerjean.proj.data.dao.system.SysUserInfoDao getDao() {
        return this.sysUserInfoDao;
    }

    public int save(SysUserInfo sysUserInfo) {
        int cnt = 0;
        if (sysUserInfo.getId() == null) {
            cnt = sysUserInfoDao.insertSelective(sysUserInfo);
        } else {
            cnt = sysUserInfoDao.updateByPrimaryKeySelective(sysUserInfo);
        }
        return cnt;
    }

    public int update(SysUserInfo sysUserInfo) {
        return sysUserInfoDao.updateByPrimaryKey(sysUserInfo);
    }

    public int updateSelective(SysUserInfo sysUserInfo) {
        return sysUserInfoDao.updateByPrimaryKeySelective(sysUserInfo);
    }

    public int insert(SysUserInfo sysUserInfo) {
        return sysUserInfoDao.insert(sysUserInfo);
    }

    public int insertSelective(SysUserInfo sysUserInfo) {
        return sysUserInfoDao.insertSelective(sysUserInfo);
    }

    public int batchInsert(List<SysUserInfo> list) {
        return sysUserInfoDao.batchInsert(list);
    }

    public SysUserInfo findById(long id) {
        return sysUserInfoDao.selectByPrimaryKey(id);
    }

    public SysUserInfo findByQueryContion(SysUserInfoQuery query) {
        List<SysUserInfo> list = sysUserInfoDao.selectByExample(query);
        if (null != list && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public List<SysUserInfo> queryList(SysUserInfoQuery query) {
        return sysUserInfoDao.selectByExample(query);
    }

    public int deleteById(long id) {
        return sysUserInfoDao.deleteByPrimaryKey(id);
    }

    public int delete(SysUserInfoQuery query) {
        return sysUserInfoDao.deleteByExample(query);
    }

    public SysUserInfoPage queryPageList(SysUserInfoQuery query) {
        SysUserInfoPage sysUserInfoPage = new SysUserInfoPage();
        Integer itemCount = sysUserInfoDao.countByExample(query);
        query.setItemCount(itemCount);

        if (itemCount == 0) {
            sysUserInfoPage.setValues(null);
        } else {
            sysUserInfoPage.setValues(sysUserInfoDao.selectPageByExample(query));
        }

        sysUserInfoPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
        return sysUserInfoPage;
    }

}
