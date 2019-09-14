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

import java.util.List;
import java.util.Objects;

/**
 * Message sent over a Slack channel.
 *
 * @author Greg Turnquist
 */
public class MessageEvent {

	private String type;
	private String channel;
	private String user;
	private String text;
	private List<Attachment> attachments;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Attachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<Attachment> attachments) {
		this.attachments = attachments;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MessageEvent that = (MessageEvent) o;
		return Objects.equals(type, that.type) && Objects.equals(channel, that.channel) && Objects.equals(user, that.user)
				&& Objects.equals(text, that.text) && Objects.equals(attachments, that.attachments);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type, channel, user, text, attachments);
	}

	@Override
	public String toString() {

		return "MessageEvent{" + "type='" + type + '\'' + ", channel='" + channel + '\'' + ", user='" + user + '\''
				+ ", text='" + text + '\'' + ", attachments=" + attachments + '}';
	}
}
