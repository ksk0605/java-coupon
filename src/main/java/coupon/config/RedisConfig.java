package coupon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import coupon.domain.Coupon;

@Configuration
public class RedisConfig {

	@Value("${spring.data.redis.port}")
	private int port;

	@Bean
	public LettuceConnectionFactory jedisConnectionFactory() {
		LettuceConnectionFactory connectionFactory
			= new LettuceConnectionFactory();
		connectionFactory.setPort(port);
		return connectionFactory;
	}

	@Bean
	public RedisTemplate<String, Coupon> redisTemplate(LettuceConnectionFactory connectionFactory) {
		RedisTemplate<String, Coupon> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		template.setKeySerializer(new StringRedisSerializer());

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());

		Jackson2JsonRedisSerializer<Coupon> serializer = new Jackson2JsonRedisSerializer<>(objectMapper, Coupon.class);

		template.setValueSerializer(serializer);
		template.setHashValueSerializer(serializer);

		return template;
	}
}
