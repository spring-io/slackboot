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

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

import io.spring.slackboot.core.domain.BotLoggedInEvent;
import io.spring.slackboot.core.domain.MessageEvent;
import io.spring.slackboot.core.domain.Self;
import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SelfAwareSlackCommandTests.TestConfig.class)
public class SelfAwareSlackCommandTests {

	SelfAwareSlackCommand selfAwareSlackCommand;

	@MockBean SlackService slackService;

	@MockBean SlackBootProperties slackBootProperties;

	@Before
	public void setUp() {

		this.selfAwareSlackCommand = new SelfAwareSlackCommand(slackService, slackBootProperties) {
			@Override
			protected boolean also(MessageEvent message) {
				return message.getText().contains("should respond");
			}

			@Override
			public void handle(MessageEvent message) {
				// We don't care about this, since it's just an abstract class
			}
		};
	}

	@Test
	public void shouldHandleReceivingLoggedInEvent() {

		// given
		Self self = new Self("abc123", "slackboot");
		BotLoggedInEvent event = new BotLoggedInEvent(self);

		// when
		selfAwareSlackCommand.onApplicationEvent(event);

		then(selfAwareSlackCommand).extracting("self").extracting("id", "name").contains(tuple("abc123", "slackboot"));
	}

	@Test
	public void shouldIgnoreMessagesNotMentioningTheBot() {

		// given
		Self self = new Self("abc123", "slackboot");
		BotLoggedInEvent event = new BotLoggedInEvent(self);
		selfAwareSlackCommand.onApplicationEvent(event);

		MessageEvent message = new MessageEvent();
		message.setText("should not match on this message");

		// when-then
		then(selfAwareSlackCommand.match(message)).isFalse();
	}

	@Test
	public void shouldNotRespondToMessageNamingTheBotButMissingKeyPhrase() {

		// given
		Self self = new Self("abc123", "slackboot");
		BotLoggedInEvent event = new BotLoggedInEvent(self);
		selfAwareSlackCommand.onApplicationEvent(event);

		MessageEvent message = new MessageEvent();
		message.setText("<@abc123> should not respond to this");

		// when-then
		then(selfAwareSlackCommand.match(message)).isFalse();
	}

	@Test
	public void shouldRespondToMessagesNamingTheBotAndWithKeyPhrase() {

		// given
		Self self = new Self("abc123", "slackboot");
		BotLoggedInEvent event = new BotLoggedInEvent(self);
		selfAwareSlackCommand.onApplicationEvent(event);

		MessageEvent message = new MessageEvent();
		message.setText("<@abc123> should respond to this");

		// when-then
		then(selfAwareSlackCommand.match(message)).isTrue();
	}

	@Configuration
	static class TestConfig {

	}
}
