package com.tuprimernegocio.library.database;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(DatabaseModuleConfiguration.class)
@Configuration
public @interface DatabaseModule {
}