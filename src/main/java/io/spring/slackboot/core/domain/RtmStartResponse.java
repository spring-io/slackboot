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

/**
 * First response when opening a WebSocket with Slack's RTM service.
 *
 * @author Greg Turnquist
 */
public class RtmStartResponse {

	private boolean ok;
	private String url;
	private Self self;

	public RtmStartResponse() {}

	public RtmStartResponse(boolean ok, String url, Self self) {

		this.ok = ok;
		this.url = url;
		this.self = self;
	}

	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Self getSelf() {
		return self;
	}

	public void setSelf(Self self) {
		this.self = self;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		RtmStartResponse that = (RtmStartResponse) o;
		return ok == that.ok && Objects.equals(url, that.url) && Objects.equals(self, that.self);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ok, url, self);
	}

	@Override
	public String toString() {
		return "RtmStartResponse{" + "ok=" + ok + ", url='" + url + '\'' + ", self=" + self + '}';
	}
}
