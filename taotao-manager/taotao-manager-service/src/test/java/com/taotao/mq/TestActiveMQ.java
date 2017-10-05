package com.taotao.mq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

public class TestActiveMQ {
	
	@Test
	public void TestQueueProducer() throws Exception {
		//创建一个连接工程ConnectionFactory对象，指定服务的IP和端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616") ;
		//使用ConnectionFactory对象，创建一个connection对象
		Connection connection = connectionFactory.createConnection() ;
		//开启连接，调用start方法
		connection.start(); 
		//使用connection对象创建一Session对象
		//第一个参数：是否开启事务，一般不开启,第二个参数才有意义
		//第二个参数：消息应答模式，手动应答和自动应答，一般为自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE) ;
		//使用Session对象创建一个destination对象，目的地有两种queue和topic
		Queue queue = session.createQueue("test-queue") ;
		//使用Session对象创建一个Producer对象
		MessageProducer producer = session.createProducer(queue) ;
		//使用producer发送消息
		TextMessage textMessage = new ActiveMQTextMessage() ;
		textMessage.setText("使用activemq发送的队列消息2");
//		TextMessage textMessage2 = session.createTextMessage("使用activemq发送的队列消息") ;
		producer.send(textMessage);
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testConsumer() throws Exception {
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616") ;
		Connection connection = connectionFactory.createConnection() ;
		connection.start(); 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE) ;
		Queue queue = session.createQueue("test-queue") ;
		//使用Session创建一个消费者
		MessageConsumer consumer = session.createConsumer(queue) ;
		//使用消费者对象接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印消息
				TextMessage textMessage = (TextMessage) message ;
				String text = "" ;
				try {
					text = textMessage.getText() ;
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read() ;
		
		/*while(true) {
			Message message = consumer.receive(3000) ;
			if(message == null) {
				break ;
			}
			//打印消息
			TextMessage textMessage = (TextMessage) message ;
			String text = "" ;
			try {
				text = textMessage.getText() ;
				System.out.println(text);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	@Test
	public void testTopicProducer() throws Exception {
		//创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616") ;
		//使用连接工厂创建一个连接
		Connection connection = connectionFactory.createConnection() ;
		//开启连接
		connection.start(); 
		//使用连接创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE) ;
		//使用session创建一个topic
		Topic topic = session.createTopic("test-topic") ;
		//使用session创建一个producer，指定目的地topic
		MessageProducer producer = session.createProducer(topic) ;
		//创建一个textMessage对象
		TextMessage textMessage = session.createTextMessage("这是我的topic") ;
		//使用producer对象发送消息
		producer.send(textMessage); 
		//关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	@Test
	public void testTopicConsumer() throws Exception {
		//创建一个连接工厂对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.128:61616") ;
		//使用连接工厂创建一个连接
		Connection connection = connectionFactory.createConnection() ;
		//开启连接
		connection.start(); 
		//使用连接创建一个session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE) ;
		//使用session创建一个topic
		Topic topic = session.createTopic("test-topic") ;
		//使用session创建一个消费者
		MessageConsumer consumer = session.createConsumer(topic) ;
		System.out.println("消费者3");
		//使用消费者对象接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印消息
				TextMessage textMessage = (TextMessage) message ;
				String text = "" ;
				try {
					text = textMessage.getText() ;
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.in.read() ;
	}
}
