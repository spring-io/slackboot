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

import io.spring.slackboot.core.domain.SlackBootProperties;
import io.spring.slackboot.core.services.SlackService;

import java.util.Objects;

import org.springframework.boot.actuate.metrics.CounterService;

/**
 * Convenience base class for {@link SlackCommand}s. That way, commands don't have to inject these commonly used things
 * in every single command.
 *
 * @author Greg Turnquist
 */
public abstract class AbstractSlackCommand implements SlackCommand {

	private SlackService slackService;
	private SlackBootProperties slackBootProperties;
	private CounterService counterService;

	public AbstractSlackCommand(SlackService slackService, SlackBootProperties slackBootProperties,
			CounterService counterService) {

		this.slackService = slackService;
		this.slackBootProperties = slackBootProperties;
		this.counterService = counterService;
	}

	public SlackService getSlackService() {
		return slackService;
	}

	public SlackBootProperties getSlackBootProperties() {
		return slackBootProperties;
	}

	public CounterService getCounterService() {
		return counterService;
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
				&& Objects.equals(slackBootProperties, that.slackBootProperties)
				&& Objects.equals(counterService, that.counterService);
	}

	@Override
	public int hashCode() {
		return Objects.hash(slackService, slackBootProperties, counterService);
	}

	@Override
	public String toString() {
		return "AbstractSlackCommand{" + "slackService=" + slackService + ", slackBootProperties=" + slackBootProperties
				+ ", counterService=" + counterService + '}';
	}
}
