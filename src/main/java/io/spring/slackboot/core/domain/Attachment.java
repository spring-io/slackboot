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
 * Some Slack messages put extra information in {@link Attachment}s.
 *
 * @author Greg Turnquist
 */
public class Attachment {

	private String fallback;
	private String text;
	private String pretext;

	public String getFallback() {
		return fallback;
	}

	public void setFallback(String fallback) {
		this.fallback = fallback;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPretext() {
		return pretext;
	}

	public void setPretext(String pretext) {
		this.pretext = pretext;
	}

	public boolean contains(String matchingText) {
		return this.text != null && this.text.contains(matchingText);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Attachment that = (Attachment) o;
		return Objects.equals(fallback, that.fallback) && Objects.equals(text, that.text)
				&& Objects.equals(pretext, that.pretext);
	}

	@Override
	public int hashCode() {
		return Objects.hash(fallback, text, pretext);
	}

	@Override
	public String toString() {
		return "Attachment{" + "fallback='" + fallback + '\'' + ", text='" + text + '\'' + ", pretext='" + pretext + '\''
				+ '}';
	}
}
