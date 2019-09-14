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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

/**
 * Convenience base class for {@link SlackCommand}s. That way, commands don't have to inject these commonly used things
 * in every single command.
 *
 * @author Greg Turnquist
 */
public abstract class AbstractSlackCommand implements SlackCommand {

	private AtomicLong id = new AtomicLong(1);
	private SlackService slackService;
	private SlackBootProperties slackBootProperties;

	public AbstractSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties) {

		this.slackService = slackService;
		this.slackBootProperties = slackBootProperties;
	}

	public SlackService getSlackService() {
		return slackService;
	}

	public SlackBootProperties getSlackBootProperties() {
		return slackBootProperties;
	}

	public long getId() {
		return this.id.getAndIncrement();
	}

	public String getToken() {
		return this.slackBootProperties.getToken();
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		AbstractSlackCommand that = (AbstractSlackCommand) o;
		return Objects.equals(slackService, that.slackService)
				&& Objects.equals(slackBootProperties, that.slackBootProperties);
	}

	@Override
	public int hashCode() {
		return Objects.hash(slackService, slackBootProperties);
	}

	@Override
	public String toString() {
		return "AbstractSlackCommand{" + "slackService=" + slackService + ", slackBootProperties=" + slackBootProperties
				+ '}';
	}
}
