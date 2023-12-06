///*
// * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
// * <p>
// * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.gnu.org/licenses/lgpl.html
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package io.ihankun.framework.cache.v2.config;
//
//import io.ihankun.framework.cache.v2.cache.MicaRedisCache;
//import io.ihankun.framework.cache.v2.pubsub.RPubSubListenerDetector;
//import io.ihankun.framework.cache.v2.pubsub.RPubSubPublisher;
//import io.ihankun.framework.cache.v2.pubsub.RedisPubSubPublisher;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.listener.RedisMessageListenerContainer;
//import org.springframework.data.redis.serializer.RedisSerializer;
//
///**
// * Redisson pub/sub 发布配置
// *
// * @author L.cm
// */
//@Configuration
//public class RedisPubSubConfiguration {
//
//	@Bean
//	@ConditionalOnMissingBean
//	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
//		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setConnectionFactory(connectionFactory);
//		return container;
//	}
//
//	@Bean
//	public RPubSubPublisher topicEventPublisher(MicaRedisCache redisCache,
//												RedisSerializer<Object> redisSerializer) {
//		return new RedisPubSubPublisher(redisCache, redisSerializer);
//	}
//
//	@Bean
//	public RPubSubListenerDetector topicListenerDetector(RedisMessageListenerContainer redisMessageListenerContainer,
//														 RedisSerializer<Object> redisSerializer) {
//		return new RPubSubListenerDetector(redisMessageListenerContainer, redisSerializer);
//	}
//
//}
