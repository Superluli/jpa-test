package com.superluli.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ComponentScan("com.superluli.jpa")
@EnableTransactionManagement
@EnableCaching
public class Application {
	
	@Bean
	public CacheManager getCacheManager(){
		
		return new EhCacheCacheManager();
	} 
	
	public static void main(String[] args) throws Exception {
		SpringApplication.run(Application.class, args);
	}
}
