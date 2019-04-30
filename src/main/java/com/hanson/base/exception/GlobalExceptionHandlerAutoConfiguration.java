package com.hanson.base.exception;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Create by hanlin on 2018年10月19日
 **/
@Configuration
//匹配com.hanson.exception.handler.enabled 为true时开启访问日志,如果未配置,默认为true
@ConditionalOnProperty(prefix = "com.hanson.exception.handler", name = "enable", havingValue = "true",  matchIfMissing = true)
public class GlobalExceptionHandlerAutoConfiguration {
	
	/**
	 * 全局日志处理
	 * @author Hanson
	 */
	@Configuration
	@ConditionalOnMissingBean(GlobalExceptionAdvice.class)
	public static class EmbeddedGlobalExceptionHandler {
		@Bean
        public GlobalExceptionAdvice create() {
            return new GlobalExceptionAdvice();
        }
	}
}
