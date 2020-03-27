package com.healerjean.proj.utils;

import com.healerjean.proj.dto.CompanyDTO;
import com.healerjean.proj.dto.UserDTO;
import com.healerjean.proj.pojo.Company;
import com.healerjean.proj.pojo.User;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {



    public static UserDTO demoToDTO(User user) {
        UserDTO dto = new UserDTO();
        if (user != null) {
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setId(user.getId());
            dto.setName(user.getName());
            dto.setCity(user.getCity());
            dto.setStatus(user.getStatus());
            dto.setCreateTime(user.getCreateTime());
            dto.setUpdateTime(user.getUpdateTime());
        }
        return dto;
    }

    public static User dtoToUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        user.setCreateTime(userDTO.getCreateTime());
        user.setUpdateTime(userDTO.getUpdateTime());
        return user;
    }


    public static Company dtoToCompany(CompanyDTO dto) {
        Company company = new Company();
        company.setId(dto.getId());
        company.setCreateTime(dto.getCreateTime());
        company.setStatus(dto.getStatus());
        company.setCompanyNameEnglish(dto.getCompanyNameEnglish());
        company.setName(dto.getName());
        company.setUpdateTime(dto.getUpdateTime());
        return company;
    }


    public static CompanyDTO companyToDTO(Company company) {
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setCreateTime(company.getCreateTime());
        dto.setStatus(company.getStatus());
        dto.setCompanyNameEnglish(company.getCompanyNameEnglish());
        dto.setName(company.getName());
        dto.setUpdateTime(company.getUpdateTime());
        return dto;
    }

    public static void main(String[] args) {
        CodeAutoUtils.beanCopy(CompanyDTO.class, Company.class, "dto", "company");
    }


}
