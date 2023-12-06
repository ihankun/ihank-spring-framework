/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.ihankun.framework.cache.v2.ratelimiter;

import io.ihankun.framework.common.utils.CharPool;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * redis 限流服务
 *
 * @author dream.lu
 */
@RequiredArgsConstructor
public class RedisRateLimiterClient implements RateLimiterClient {
	/**
	 * redis 限流 key 前缀
	 */
	private static final String REDIS_KEY_PREFIX = "limiter:";
	/**
	 * 失败的默认返回值
	 */
	private static final long FAIL_CODE = 0;
	/**
	 * redisTemplate
	 */
	private final StringRedisTemplate redisTemplate;
	/**
	 * redisScript
	 */
	private final RedisScript<Long> script;
	/**
	 * env
	 */
	private final Environment environment;

	@Override
	public boolean isAllowed(String key, long max, long ttl, TimeUnit timeUnit) {
		// redis key
		String redisKeyBuilder = REDIS_KEY_PREFIX +
			getApplicationName(environment) + CharPool.COLON + key;
		List<String> keys = Collections.singletonList(redisKeyBuilder);
		// 转为毫秒
		long ttlMillis = timeUnit.toMillis(ttl);
		// 执行命令
		Long result = this.redisTemplate.execute(this.script, keys, Long.toString(max), Long.toString(ttlMillis));
		return result != null && result != FAIL_CODE;
	}

	private static String getApplicationName(Environment environment) {
		return environment.getProperty("spring.application.name", "");
	}

}
