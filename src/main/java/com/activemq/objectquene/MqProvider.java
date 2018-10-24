package com.activemq.objectquene;

import com.activemq.bean.StudentDetail;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.SimpleDateFormat;

/**
 * @Title: activemq 对象消息队列 异步方式
 * @depression: 点对点模式
 * @author: eric
 * @date: 2018/10/22 14:39
 * @throws: JMSException
 */
public class MqProvider {
    public static final int SENT_NUM = 5;
    public static final String URL = "tcp://192.168.217.130:61616";

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, URL);
        Connection connection = null;
        Session session = null;
        Destination destination = null;
        MessageProducer messageProducer = null;
        ObjectMessage objectMessage = null;
        StudentDetail studentDetail = null;

        try {
            //利用activemq工厂生成连接对象
            connection = connectionFactory.createConnection();
            connection.start();

            /**
             * 一个发送或接收消息的线程
             * 当前参数arg1：为'TRUE'时，开启事务发送，屏蔽arg2参数的设置，只有arg1参数为'false'时，arg2的参数才有作用
             * Session.AUTO_ACKNOWLEDGE为自动确认，客户端发送和接收消息不需要做额外的工作。
             * Session.CLIENT_ACKNOWLEDGE为客户端确认。客户端接收到消息后，必须调用javax.jms.Message的acknowledge方法。jms服务器才会删除消息。
             * DUPS_OK_ACKNOWLEDGE允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认。在需要考虑资源使用时，这种模式非常有效。
             * */
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            //生成消息队列
            destination = session.createQueue("Object-up");

            //生成消息提供者
            messageProducer = session.createProducer(destination);

            //设置消息队列 持久化方式
            messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long time = null;
            String dateStr = "";

            for (int i = 0; i < SENT_NUM; i++) {
                time = new Long(System.currentTimeMillis());
                dateStr = format.format(time);
                studentDetail = new StudentDetail("学员：" + i, " 年龄：" + i);
                objectMessage = session.createObjectMessage(studentDetail);
                messageProducer.send(objectMessage);
            }

//            session.commit();

            System.out.println("JMSMessageID:" + objectMessage.getJMSMessageID());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
