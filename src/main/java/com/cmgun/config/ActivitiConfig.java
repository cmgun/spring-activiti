package com.cmgun.config;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

/**
 * 注入数据库配置和事务管理到Activiti
 *
 * @author chenqilin
 * @Date 2019/7/2
 */
//@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(@Qualifier("dataSource") DataSource dataSource,
                                                                             @Qualifier("transactionManager") PlatformTransactionManager transactionManager,
                                                                             SpringAsyncExecutor springAsyncExecutor) throws IOException {
        return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
    }
}
