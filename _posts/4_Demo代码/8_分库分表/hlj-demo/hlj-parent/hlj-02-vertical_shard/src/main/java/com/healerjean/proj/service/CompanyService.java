package com.healerjean.proj.service;

import com.healerjean.proj.dto.CompanyDTO;

/**
 * @author HealerJean
 * @ClassName CompanyService
 * @date 2020/3/27  15:20.
 * @Description
 */
public interface CompanyService {


    CompanyDTO insert(CompanyDTO companyDTO);

    CompanyDTO findById(Long id);
}
