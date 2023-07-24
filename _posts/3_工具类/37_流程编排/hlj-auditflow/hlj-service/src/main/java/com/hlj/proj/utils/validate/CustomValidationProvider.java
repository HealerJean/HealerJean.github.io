package com.hlj.proj.utils.validate;

import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.internal.engine.ConfigurationImpl;

import javax.validation.Configuration;
import javax.validation.ValidatorFactory;
import javax.validation.spi.BootstrapState;
import javax.validation.spi.ConfigurationState;
import javax.validation.spi.ValidationProvider;

/**
 * @author HealerJean
 * @ClassName CustomValidationProvider
 * @date 2019/8/9  14:54.
 * @Description
 */
public class CustomValidationProvider implements ValidationProvider<HibernateValidatorConfiguration> {

    @Override
    public HibernateValidatorConfiguration createSpecializedConfiguration(BootstrapState state) {
        return HibernateValidatorConfiguration.class.cast(new ConfigurationImpl(this));
    }

    @Override
    public Configuration<?> createGenericConfiguration(BootstrapState state) {
        return new ConfigurationImpl(state);
    }

    @Override
    public ValidatorFactory buildValidatorFactory(ConfigurationState configurationState) {
        return new CustomValidatorFactoryImpl(configurationState);
    }
}
