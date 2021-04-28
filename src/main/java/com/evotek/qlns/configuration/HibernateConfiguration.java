package com.evotek.qlns.configuration;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/*
 * LinhLH
 */

@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:data.properties" })
@ComponentScan({ "com.evotek.qlns" })
public class HibernateConfiguration {
	
	private static final Logger _log = LogManager.getLogger(HibernateConfiguration.class);
	//database connection information
	@Value( "${jdbc.url}" )
	private String jdbcUrl;
	
	@Value( "${jdbc.driverClassName}" )
	private String jdbcDriverClassName;
	
	@Value( "${jdbc.user}" )
	private String jdbcUser;
	
	@Value( "${jdbc.pass}" )
	private String jdbcPass;
	
	//hibernate configuration
	@Value( "${hibernate.dialect}" )
	private String dialect;
	
	@Value( "${hibernate.show_sql}" )
	private String showSql;
	
	@Value( "${hibernate.format_sql}" )
	private String formatSql;
	
	@Value( "${hibernate.current_session_context_class}" )
	private String currentSessionContextClass;
	
	@Value( "${hibernate.connection.provider_class}" )
	private String connectionProviderClass;
	
	@Value( "${hibernate.connection.release_mode}" )
	private String connectionReleaseMode;
	
	@Value( "${hibernate.connection.charSet}" )
	private String connectionCharSet;
	
	//c3p0
	@Value( "${hibernate.c3p0.min_size}" )
	private int minSize;
	
	@Value( "${hibernate.c3p0.max_size}" )
	private int maxSize;
	
	@Value( "${hibernate.c3p0.acquire_increment}" )
	private int acquireIncrement;
	
	@Value( "${hibernate.c3p0.timeout}" )
	private int timeout;
	
	@Value( "${hibernate.c3p0.max_statements}" )
	private int maxStatements;
	
	@Value( "${hibernate.c3p0.idle_test_period}" )
	private int idleTestPeriod;
	
	@Bean
    public DataSource dataSource(){
        ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
        //database information
        try {
			pooledDataSource.setDriverClass(this.jdbcDriverClassName);
			
			pooledDataSource.setUser(this.jdbcUser);
	        pooledDataSource.setPassword(this.jdbcPass);
	        pooledDataSource.setJdbcUrl(this.jdbcUrl);
	        
	        //c3p0
	        pooledDataSource.setMaxPoolSize(this.maxSize);
	        pooledDataSource.setMinPoolSize(this.minSize);
	        pooledDataSource.setAcquireIncrement(this.acquireIncrement);
	        pooledDataSource.setCheckoutTimeout(this.timeout);
	        pooledDataSource.setMaxStatements(this.maxStatements);
	        pooledDataSource.setIdleConnectionTestPeriod(this.idleTestPeriod);
		} catch (PropertyVetoException e) {
			_log.error(e.getMessage(), e);
			
			_log.error("Error occurred when configure dataSource");
		}
        

        return pooledDataSource;
    }
	
	@Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        
        sessionFactory.setDataSource(dataSource());
        
        sessionFactory.setPackagesToScan(new String[] {
            "com.evotek.qlns.model"
        });
        
        sessionFactory.setHibernateProperties(hibernateProperties());
        
        return sessionFactory;
    }
	
	private Properties hibernateProperties() {
        Properties properties = new Properties();
        
        properties.put("hibernate.dialect", this.dialect);
        properties.put("hibernate.show_sql", this.showSql);
        properties.put("hibernate.format_sql", this.formatSql);
        properties.put("hibernate.current_session_context_class", this.currentSessionContextClass);
        properties.put("hibernate.connection.provider_class", this.connectionProviderClass);
        properties.put("hibernate.connection.release_mode", this.connectionReleaseMode);
        properties.put("hibernate.connection.charSet", this.connectionCharSet);
        
        return properties;
    }
	
	@Bean
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        
        transactionManager.setSessionFactory(sessionFactory().getObject());
        
        return transactionManager;
    }
}
