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

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service that allows scheduling shutdown tasks, in the event something doesn't report back in a suitable timeframe.
 *
 * @author Greg Turnquist
 */
@Service
public class DeadmanSwitch {

	private static final Logger log = LoggerFactory.getLogger(DeadmanSwitch.class);

	private final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
	private final Map<String, ScheduledFuture<?>> switches = new HashMap<>();

	public void reset(String context, Duration duration) {

		// Clear (and cancel) out previous switch at this context
		Optional.ofNullable(switches.get(context))
			.ifPresent(scheduledFuture -> {
				log.debug("Old deadman switch '" + context + "' canceled.");
				scheduledFuture.cancel(true);
			});

		log.debug("Scheduling deadman switch '" + context + "'");

		// Schedule new deadman switch at the context.
		switches.put(context, scheduledExecutorService.schedule(() -> {
			log.error("Time's up! " + context + " consider stale.");
			System.exit(99);
		}, duration.getSeconds(), TimeUnit.SECONDS));
	}

}
