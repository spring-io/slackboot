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
package io.spring.slackboot.core.domain;

import java.util.Objects;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Bootified property settings.
 *
 * @author Greg Turnquist
 */
@Component
@ConfigurationProperties(prefix = "slack.boot")
public class SlackBootProperties {

	private String token;
	private String githubToken;
	private boolean debugFeign = false;

	private long deadmanLimitMinutes = 1L;

	// Pure debugging tool, meant to force a deadman switch to kick in.
	private boolean randomNap = false;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getGithubToken() {
		return githubToken;
	}

	public void setGithubToken(String githubToken) {
		this.githubToken = githubToken;
	}

	public boolean isDebugFeign() {
		return debugFeign;
	}

	public void setDebugFeign(boolean debugFeign) {
		this.debugFeign = debugFeign;
	}

	public long getDeadmanLimitMinutes() {
		return deadmanLimitMinutes;
	}

	public void setDeadmanLimitMinutes(long deadmanLimitMinutes) {
		this.deadmanLimitMinutes = deadmanLimitMinutes;
	}

	public boolean isRandomNap() {
		return randomNap;
	}

	public void setRandomNap(boolean randomNap) {
		this.randomNap = randomNap;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SlackBootProperties that = (SlackBootProperties) o;
		return debugFeign == that.debugFeign && deadmanLimitMinutes == that.deadmanLimitMinutes
				&& randomNap == that.randomNap && Objects.equals(token, that.token)
				&& Objects.equals(githubToken, that.githubToken);
	}

	@Override
	public int hashCode() {
		return Objects.hash(token, githubToken, debugFeign, deadmanLimitMinutes, randomNap);
	}

	@Override
	public String toString() {

		return "SlackBootProperties{" + "token='" + token + '\'' + ", githubToken='" + githubToken + '\'' + ", debugFeign="
				+ debugFeign + ", deadmanLimitMinutes=" + deadmanLimitMinutes + ", randomNap=" + randomNap + '}';
	}
}
