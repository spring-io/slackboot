/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.spring.slackboot.core;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.Mockito.*;

import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Greg Turnquist
 */
@SpringBootTest(classes = AbstractSlackCommandTests.TestConfig.class, properties = {"slack.boot.token=token1"})
public class AbstractSlackCommandTests {

	@Autowired
	AbstractSlackCommand slackCommand;

	@Test
	public void shouldInitializeAllRequiredServicesThroughTheContainer() {

		then(slackCommand.getSlackBootProperties()).isNotNull();
		then(slackCommand.getSlackService()).isNotNull();

		then(slackCommand.getToken()).isEqualTo("token1");
	}

	@Configuration
	@EnableConfigurationProperties(SlackBootProperties.class)
	static class TestConfig {

		@Autowired
		SlackService slackService;

		@Autowired
		SlackBootProperties slackBootProperties;

		@Bean
		AbstractSlackCommand slackCommand() {

			return new AbstractSlackCommand(slackService, slackBootProperties) {
				@Override
				public boolean match(MessageEvent message) {
					return true;
				}

				@Override
				public void handle(MessageEvent message) {
					// We don't care about message handling here.
				}
			};
		}

		@Bean
		SlackService slackService() {
			return mock(SlackService.class);
		}
	}
}
