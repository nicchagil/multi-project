package com.nicchagil.rabbitmqjavaclientexercies.queue;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.nicchagil.rabbitmqjavaclientexercies.util.RabbitMQClientUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class QueueCustomer {

    private final static String QUEUE_NAME = "hello world";

    public static void main(String[] argv) throws java.io.IOException,
            java.lang.InterruptedException, TimeoutException {

    	Connection connection = null;
        Channel channel = null;
        try {
            ConnectionFactory factory = RabbitMQClientUtil.getConnectionFactory(); // 获取连接工厂
            connection = factory.newConnection(); // 创建连接
            channel = connection.createChannel(); // 创建信道

	        // 声明一个队列：名称、持久性的（重启仍存在此队列）、非私有的、非自动删除的
	        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
	        System.out.println("Waiting for messages.");
	
	        /* 定义消费者 */
	        Consumer consumer = new DefaultConsumer(channel) {
	            @Override
	            public void handleDelivery(String consumerTag, Envelope envelope,
	                    AMQP.BasicProperties properties, byte[] body)
	                    throws IOException {
	                String message = new String(body, "UTF-8");
	                System.out.println("Received the message -> " + message);
	            }
	        };
	        
	        // 将消费者绑定到队列，并设置自动确认消息（即无需显示确认，如何设置请慎重考虑）
	        channel.basicConsume(QUEUE_NAME, true, consumer);
	        
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
        	/*
            channel.close(); // 关闭信道
            connection.close(); // 关闭连接
            */
        }
    }
}