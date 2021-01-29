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

import static org.mockito.Mockito.*;

import io.spring.slackboot.core.domain.BotLoggedInEvent;
import io.spring.slackboot.core.domain.RtmStartResponse;
import io.spring.slackboot.core.domain.Self;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Greg Turnquist
 */
@SpringBootTest(classes = SlackBootInitializerTests.TestConfig.class)
public class SlackBootInitializerTests {

	@Autowired SlackBootInitializer slackBootInitializer;

	@Autowired SlackService slackService;

	@MockBean SlackWebSocketHandler slackWebSocketHandler;

	@Autowired CountDownLatch countDownLatch;

	@Test
	public void noop() throws InterruptedException {

		countDownLatch.await();

		verifyNoInteractions(slackWebSocketHandler);
	}

	@Configuration
	@EnableConfigurationProperties(SlackBootProperties.class)
	static class TestConfig {

		@Bean
		CountDownLatch countDownLatch() {
			return new CountDownLatch(1);
		}

		@Bean
		SlackService slackService() {

			return new SlackService() {
				@Override
				public RtmStartResponse rtmStart(@RequestParam("token") String token) {
					return new RtmStartResponse(true, "wss://example.com", new Self("abc123", "slackboot"));
				}

				@Override
				public void sendMessage(@RequestParam("token") String token, @RequestParam("text") String text,
						@RequestParam("channel") String channel, @RequestParam("as_user") boolean asUser) {
					// We don't care for this test case
				}

				@Override
				public void sendPing(@RequestParam("id") long id, @RequestParam("token") String token,
						@RequestParam("type") String type, @RequestParam("channel") String channel,
						@RequestParam("text") String text) {
					// We don't care for this test case
				}
			};
		}

		@Bean
		ApplicationListener<BotLoggedInEvent> applicationListener(CountDownLatch countDownLatch) {
			return event -> countDownLatch.countDown();
		}

		@Bean
		SlackBootInitializer slackBootInitializer(SlackService slackService, SlackBootProperties slackBootProperties,
				SlackWebSocketHandler slackWebSocketHandler) {
			return new SlackBootInitializer(slackService, slackBootProperties, slackWebSocketHandler);
		}
	}
}
