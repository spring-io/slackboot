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

import io.spring.slackboot.core.domain.MessageEvent;

/**
 * Base interface for all custom Slack commands. Includes a check for whether or not command fits the incoming
 * {@link MessageEvent} and also a method to process it.
 *
 * @author Greg Turnquist
 */
public interface SlackCommand {

	boolean match(MessageEvent message);

	void handle(MessageEvent message);

}
