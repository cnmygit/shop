package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * 接收activeMQ队列消息的监听器
 * @author Leo
 *
 */
public class MyMessageListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		//取消息内容
		try {
			TextMessage textMessage = (TextMessage) message ;
			//取内容
			String text = textMessage.getText() ;
			System.out.println(text);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
