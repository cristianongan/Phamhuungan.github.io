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
	@Value("${hibernate.c3p0.acquire_increment}")
	private int acquireIncrement;

	@Value("${hibernate.connection.charSet}")
	private String connectionCharSet;

	@Value("${hibernate.connection.provider_class}")
	private String connectionProviderClass;

	@Value("${hibernate.connection.release_mode}")
	private String connectionReleaseMode;

	@Value("${hibernate.current_session_context_class}")
	private String currentSessionContextClass;

	// hibernate configuration
	@Value("${hibernate.dialect}")
	private String dialect;

	@Value("${spring.datasource.driverClassName}")
	private String driverClassName;

	@Value("${hibernate.format_sql}")
	private String formatSql;

	@Value("${hibernate.c3p0.idle_test_period}")
	private int idleTestPeriod;

	@Value("${hibernate.c3p0.max_size}")
	private int maxSize;

	@Value("${hibernate.c3p0.max_statements}")
	private int maxStatements;

	// c3p0
	@Value("${hibernate.c3p0.min_size}")
	private int minSize;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${hibernate.show_sql}")
	private String showSql;

	@Value("${hibernate.c3p0.timeout}")
	private int timeout;

	// database connection information
	@Value("${spring.datasource.url}")
	private String url;

	@Value("${spring.datasource.username}")
	private String username;

	@Bean
	public DataSource dataSource() {
		ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
		// database information
		try {
			pooledDataSource.setDriverClass(this.driverClassName);
			pooledDataSource.setUser(this.username);
			pooledDataSource.setPassword(this.password);
			pooledDataSource.setJdbcUrl(this.url);

			// c3p0
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
	public HibernateTransactionManager getTransactionManager() {
		HibernateTransactionManager transactionManager = new HibernateTransactionManager();

		transactionManager.setSessionFactory(sessionFactory().getObject());

		return transactionManager;
	}

	private Properties hibernateProperties() {
		Properties properties = new Properties();

		properties.put("hibernate.dialect", this.dialect);
		properties.put("hibernate.show_sql", this.showSql);
		properties.put("hibernate.format_sql", this.formatSql);
		properties.put("hibernate.current_session_context_class", this.currentSessionContextClass);
		properties.put("hibernate.connection.provider_class", this.connectionProviderClass);

		properties.put("hibernate.connection.driver_class", this.driverClassName);
		properties.put("hibernate.connection.url", this.url);
		properties.put("hibernate.connection.username", this.username);
		properties.put("hibernate.connection.password", this.password);

		properties.put("hibernate.connection.release_mode", this.connectionReleaseMode);
		properties.put("hibernate.connection.charSet", this.connectionCharSet);

		// c3po
		properties.put("hibernate.c3p0.min_size", this.minSize);
		properties.put("hibernate.c3p0.max_size", this.maxSize);
		properties.put("hibernate.c3p0.timeout", this.timeout);
		properties.put("hibernate.c3p0.max_statements", this.maxStatements);

		return properties;
	}

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

		sessionFactory.setDataSource(dataSource());

		sessionFactory.setPackagesToScan(new String[] { "com.evotek.qlns.model" });

		sessionFactory.setHibernateProperties(hibernateProperties());

		return sessionFactory;
	}
}
