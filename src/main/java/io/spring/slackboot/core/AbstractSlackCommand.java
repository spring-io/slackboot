/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.slackboot.core;

import lombok.Getter;

import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.actuate.metrics.CounterService;

/**
 * Convenience base class for {@link SlackCommand}s. That way, commands don't have to inject these
 * commonly used things in every single command.
 *
 * @author Greg Turnquist
 */
public abstract class AbstractSlackCommand implements SlackCommand, BeanFactoryAware, InitializingBean {

	private @Getter SlackService slackService;
	private @Getter SlackBootProperties slackBootProperties;
	private @Getter CounterService counterService;
	private BeanFactory beanFactory;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		this.slackService = beanFactory.getBean(SlackService.class);
		this.slackBootProperties = beanFactory.getBean(SlackBootProperties.class);
		this.counterService = beanFactory.getBean(CounterService.class);
	}

	/**
	 * Shortcut to the oauth token, allowing other commands to be invoked.
	 *
	 * @return
	 */
	public String getToken() {
		return slackBootProperties.getToken();
	}
}
