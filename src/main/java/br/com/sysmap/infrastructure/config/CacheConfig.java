package br.com.sysmap.infrastructure.config;

import br.com.sysmap.application.domain.ServiceRequestType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * Created by ecellani on 03/06/17.
 */
@Configuration
public class CacheConfig {

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, List<ServiceRequestType>> redisTemplate() {
        RedisTemplate<String, List<ServiceRequestType>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        return redisTemplate;
    }
}
