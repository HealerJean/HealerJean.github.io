package com.healerjean.proj.data.repository.system;

import com.healerjean.proj.data.pojo.system.SysDictionaryData;
import org.springframework.data.repository.CrudRepository;

/**
 * @author HealerJean
 * @ClassName SysDictionaryDataRepository
 * @date 2019-10-17  23:07.
 * @Description
 */
public interface SysDictionaryDataRepository extends CrudRepository<SysDictionaryData, Long> {

    boolean existsByRefTypeKeyAndDataKeyAndStatus(String refTypeKey, String dataKey, String status);
}
