package com.healerjean.proj.utils;

import com.healerjean.proj.common.dto.page.PageDTO;
import org.springframework.data.domain.Page;

/**
 * @author HealerJean
 * @version 1.0v
 * @ClassName BeanUtils
 * @date 2019/6/13  20:08.
 * @Description
 */
public class BeanUtils {


    public static <T> PageDTO<T> toPageDTO(Page<T> page) {
        if (page == null || page.getContent() == null || page.getContent().size() == 0) {
            return new PageDTO(null);
        }
        return new PageDTO(page.getNumber(), page.getSize(), page.getSize(), page.getTotalPages(), page.getContent());
    }

}
