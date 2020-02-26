package com.healerjean.proj.data.manager.system;

import com.healerjean.proj.data.common.paging.Pagenation;
import com.healerjean.proj.data.dao.system.SysDictionaryDataDao;
import com.healerjean.proj.data.pojo.system.SysDictionaryData;
import com.healerjean.proj.data.pojo.system.SysDictionaryDataPage;
import com.healerjean.proj.data.pojo.system.SysDictionaryDataQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zhangyujin
 * @ClassName: SysDictionaryDataManager
 * @date 2099/1/1
 * @Description: SysDictionaryDataManager
 */
@Component("sysDictionaryDataManager")
public class SysDictionaryDataManager {

	@Autowired
	@Qualifier("sysDictionaryDataDao")
	private SysDictionaryDataDao sysDictionaryDataDao;

	public SysDictionaryDataDao getDao() {
		return this.sysDictionaryDataDao;
	}

	public int save(SysDictionaryData sysDictionaryData) {
		int cnt = 0;
		if (sysDictionaryData.getId() == null) {
			cnt = sysDictionaryDataDao.insertSelective(sysDictionaryData);
		} else {
			cnt = sysDictionaryDataDao.updateByPrimaryKeySelective(sysDictionaryData);
		}
		return cnt;
	}

	public int update(SysDictionaryData sysDictionaryData) {
		return sysDictionaryDataDao.updateByPrimaryKey(sysDictionaryData);
	}

	public int updateSelective(SysDictionaryData sysDictionaryData) {
		return sysDictionaryDataDao.updateByPrimaryKeySelective(sysDictionaryData);
	}

	public int insert(SysDictionaryData sysDictionaryData) {
		return sysDictionaryDataDao.insert(sysDictionaryData);
	}

	public int insertSelective(SysDictionaryData sysDictionaryData) {
		return sysDictionaryDataDao.insertSelective(sysDictionaryData);
	}

	public int batchInsert(List<SysDictionaryData> list){
		return sysDictionaryDataDao.batchInsert(list);
	}

	public SysDictionaryData findById(long id) {
		return sysDictionaryDataDao.selectByPrimaryKey(id);
	}

	public SysDictionaryData findByQueryContion(SysDictionaryDataQuery query) {
		List<SysDictionaryData> list = sysDictionaryDataDao.selectByExample(query);
		if(null!=list && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}

	public List<SysDictionaryData> queryList(SysDictionaryDataQuery query) {
		return sysDictionaryDataDao.selectByExample(query);
	}

	public int deleteById(long id) {
		return sysDictionaryDataDao.deleteByPrimaryKey(id);
	}

	public int delete(SysDictionaryDataQuery query) {
		return sysDictionaryDataDao.deleteByExample(query);
	}

	public SysDictionaryDataPage queryPageList(SysDictionaryDataQuery query) {
		SysDictionaryDataPage sysDictionaryDataPage = new SysDictionaryDataPage();
		Integer itemCount = sysDictionaryDataDao.countByExample(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			sysDictionaryDataPage.setValues(null);
		} else {
			sysDictionaryDataPage.setValues(sysDictionaryDataDao.selectPageByExample(query));
		}

		sysDictionaryDataPage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return sysDictionaryDataPage;
	}



	public SysDictionaryDataPage getDictDataPageLikes(SysDictionaryDataQuery query) {
		SysDictionaryDataPage typePage = new SysDictionaryDataPage();
		Integer itemCount = sysDictionaryDataDao.countDictDataLikes(query);
		query.setItemCount(itemCount);

		if (itemCount == 0) {
			typePage.setValues(null);
		} else {
			typePage.setValues(sysDictionaryDataDao.getDictDataPageLikes(query));
		}
		typePage.setPagenation(new Pagenation(query.getPageNo(), query.getPageSize(), query.getItemCount()));
		return typePage;
	}

	public List<SysDictionaryData>getDictDataLikes(SysDictionaryDataQuery query) {
		return sysDictionaryDataDao.getDictDataLikes(query);
	}

}
