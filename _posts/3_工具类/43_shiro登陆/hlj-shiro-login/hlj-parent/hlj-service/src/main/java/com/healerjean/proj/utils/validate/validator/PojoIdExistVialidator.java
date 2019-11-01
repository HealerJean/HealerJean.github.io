package com.healerjean.proj.utils.validate.validator;

import com.healerjean.proj.utils.SpringHelper;
import com.healerjean.proj.validate.anno.PojoIdExist;
import org.springframework.data.repository.CrudRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author HealerJean
 * @ClassName PojoIdExistVialidator
 * @date 2019-10-17  22:51.
 * @Description
 */
public class PojoIdExistVialidator implements ConstraintValidator<PojoIdExist, Long> {

    private String repository;

    @Override
    public void initialize(PojoIdExist constraintAnnotation) {
        repository = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext context) {
        if (id == null) {
            return true;
        }
        CrudRepository crudRepository = (CrudRepository) SpringHelper.getBeanByName(repository);
        return crudRepository.existsById(id);
    }


}
