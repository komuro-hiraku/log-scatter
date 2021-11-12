/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.xet.logscatter;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.LongStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class LogScatter implements CommandLineRunner {
	
	private final TaskExecutor taskExecutor;

	private final LogScatterProperties properties;

	private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

	@Override
	public void run(String... args) throws Exception {
		DecimalFormat formatter = new DecimalFormat("000,000,000,000,000,000,000");
		log.info("Keep-alive: {}", ((ThreadPoolTaskExecutor)taskExecutor).getKeepAliveSeconds());
		taskExecutor.execute(() -> LongStream.iterate(0, v -> v + 1)
			.peek(this::sleep)
			.mapToObj(formatter::format)
			.forEach(v -> {
				Map<String, String> map = GarbageLogUtil.getDummyBody(properties.getAttributesCount());
				try {
					log.info("{}", objectMapper.writeValueAsString(map));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
			})
		);
	}
	
	private void sleep(long v) {
		if (v % 5 == 0) {
			try {
				Thread.sleep(properties.getWaitSleepMilliseconds());
			} catch (InterruptedException e) {
				log.error("Unexpected interrupt", e);
			}
		}
	}
}
