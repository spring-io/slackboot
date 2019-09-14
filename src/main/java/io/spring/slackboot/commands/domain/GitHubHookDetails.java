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
package io.spring.slackboot.commands.domain;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Greg Turnquist
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubHookDetails {

	private int id;
	private String url;
	private GitHubHookConfig config;

	public GitHubHookDetails() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public GitHubHookConfig getConfig() {
		return config;
	}

	public void setConfig(GitHubHookConfig config) {
		this.config = config;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		GitHubHookDetails that = (GitHubHookDetails) o;
		return id == that.id && Objects.equals(url, that.url) && Objects.equals(config, that.config);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, url, config);
	}

	@Override
	public String toString() {
		return "GitHubHookDetails{" + "id=" + id + ", url='" + url + '\'' + ", config=" + config + '}';
	}
}
