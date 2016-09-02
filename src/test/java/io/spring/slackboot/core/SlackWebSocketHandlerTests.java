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
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.spring.slackboot.core.handlers.SlackEventHandler;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Turnquist
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackWebSocketHandlerTests.TestConfig.class)
public class SlackWebSocketHandlerTests {

	@Autowired
	SlackWebSocketHandler slackWebSocketHandler;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private WebSocketSession session;

	@MockBean
	private SlackEventHandler handler;


	@Test
	public void shouldHandleSimpleValidMessage() throws Exception {

		// given
		Map<String, String> source = new HashMap<>();
		source.put("type", "message");
		source.put("text", "I'm a simple message");
		TextMessage message = new TextMessage(objectMapper.writeValueAsString(source));
		given(handler.handles(any())).willReturn(true);

		// when
		slackWebSocketHandler.handleTextMessage(session, message);

		then(slackWebSocketHandler.getSlackEventHandlers()).hasSize(1);
		verify(handler).handles(source);
		verify(handler).handle(message.getPayload());
	}

	@Test
	public void shouldHandleSimpleInvalidMessage() throws Exception {

		// given
		TextMessage message = new TextMessage("This is not valid since it isn't JSON");
		given(handler.handles(any())).willReturn(true);

		// when
		slackWebSocketHandler.handleTextMessage(session, message);

		then(slackWebSocketHandler.getSlackEventHandlers()).hasSize(1);
		verifyZeroInteractions(handler);
	}

	@Test
	public void shouldHandleUnexpectedExceptionInEventHandler() throws Exception {

		// given
		Map<String, String> source = new HashMap<>();
		source.put("type", "message");
		source.put("text", "I'm a simple message");
		TextMessage message = new TextMessage(objectMapper.writeValueAsString(source));
		given(handler.handles(any())).willReturn(true);
		willThrow(new RuntimeException("Couldn't handle it!")).given(handler).handle(any());

		// when
		slackWebSocketHandler.handleTextMessage(session, message);

		then(slackWebSocketHandler.getSlackEventHandlers()).hasSize(1);
		verify(handler).handles(source);
		verify(handler).handle(message.getPayload());
	}

	@Configuration
	static class TestConfig {

		@Bean
		ObjectMapper objectMapper() {
			return new ObjectMapper();
		}

		@Bean
		SlackWebSocketHandler slackWebSocketHandler(ObjectMapper objectMapper, List<SlackEventHandler> handlers) {
			return new SlackWebSocketHandler(objectMapper, handlers);
		}
	}

}
