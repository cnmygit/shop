package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.taotao.content.jedis.JedisClient;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedisSingle() throws Exception {
		//创建连接池
		JedisPool jedisPool = new JedisPool("192.168.25.128", 6379) ;
		//从连接池中获得连接
		Jedis jedis = jedisPool.getResource() ;
		jedis.set("mytest", "1000") ;
		System.out.println(jedis.get("mytest"));
	}
	
	//连接集群测试
	@Test
	public void testJedisCluster() throws Exception { 
		//连接集群使用JedisCluster对象
		Set<HostAndPort> nodes = new HashSet<>() ;
		nodes.add(new HostAndPort("192.168.25.128", 7001)) ;
		nodes.add(new HostAndPort("192.168.25.128", 7002)) ;
		nodes.add(new HostAndPort("192.168.25.128", 7003)) ;
		nodes.add(new HostAndPort("192.168.25.128", 7004)) ;
		nodes.add(new HostAndPort("192.168.25.128", 7005)) ;
		nodes.add(new HostAndPort("192.168.25.128", 7006)) ;
		//系统中可以是单例
		JedisCluster jedisCluster = new JedisCluster(nodes) ;
		jedisCluster.set("jedisCluster", "123456") ;
		System.out.println(jedisCluster.get("jedisCluster"));
		//系统结束前，关闭jedisCluster
		jedisCluster.close(); 
	}
	
	//spring整合redis
	@Test
	public void testjedis() throws Exception {
		//加载配置文件
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml") ;
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class) ;
		jedisClient.set("client", "asdf") ;
		String result = jedisClient.get("client") ;
		System.out.println(result);
	}
}
