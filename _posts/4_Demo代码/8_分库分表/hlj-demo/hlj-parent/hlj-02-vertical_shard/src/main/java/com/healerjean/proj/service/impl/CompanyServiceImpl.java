package com.healerjean.proj.service.impl;

import com.healerjean.proj.common.enums.StatusEnum;
import com.healerjean.proj.dao.mapper.CompanyMapper;
import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.pojo.Company;
import com.healerjean.proj.service.CompanyService;
import com.healerjean.proj.utils.BeanUtils;
import com.healerjean.proj.utils.EmptyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HealerJean
 * @ClassName CompnayServiceImpl
 * @date 2020/3/27  15:20.
 * @Description
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public CompanyDTO insert(CompanyDTO companyDTO) {
        Company company = BeanUtils.dtoToCompany(companyDTO);
        company.setStatus(StatusEnum.生效.code);
        companyMapper.insert(company);
        companyDTO.setId(company.getId());
        return companyDTO;
    }

    @Override
    public CompanyDTO findById(Long id) {
        Company company = companyMapper.selectById(id);
        return company == null ? null : BeanUtils.companyToDTO(company);
    }

    @Override
    public List<CompanyDTO> list() {
        List<Company> users = companyMapper.selectList(null);
        List<CompanyDTO> list = null;
        if (!EmptyUtil.isEmpty(users)) {
            list = users.stream().map(BeanUtils::companyToDTO).collect(Collectors.toList());
        }
        return list;
    }
}
