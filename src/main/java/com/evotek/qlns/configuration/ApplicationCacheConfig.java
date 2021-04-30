/**
 * 
 */
package com.evotek.qlns.configuration;

import java.util.Arrays;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.evotek.qlns.cache.CustomKeyGenerator;

/**
 * @author LinhLH
 *
 */
@EnableCaching
@Configuration
public class ApplicationCacheConfig extends CachingConfigurerSupport {
	@Bean
	public CacheManager cacheManager() {
		SimpleCacheManager cacheManager = new SimpleCacheManager();
		Cache booksCache = new ConcurrentMapCache("books");
		cacheManager.setCaches(Arrays.asList(booksCache));
		return cacheManager;
	}

	@Bean("customKeyGenerator")
	public KeyGenerator keyGenerator() {
		return new CustomKeyGenerator();
	}
}
