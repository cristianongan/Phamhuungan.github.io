package com.evotek.qlns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = { "com.evotek.qlns.*" })
@EnableAutoConfiguration(exclude = { //
		DataSourceAutoConfiguration.class, //
		DataSourceTransactionManagerAutoConfiguration.class, //
		HibernateJpaAutoConfiguration.class })
@EnableScheduling
@EnableCaching(proxyTargetClass = true)
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class QlnsApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");

		SpringApplication.run(QlnsApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(QlnsApplication.class);
	}

}
