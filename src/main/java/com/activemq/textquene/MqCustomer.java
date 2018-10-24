package com.activemq.textquene;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.text.SimpleDateFormat;

/**
 * @Title: activemq 文本消息队列
 * @depression: 点对点模式
 * @author: eric
 * @date: 2018/10/22 14:39
 * @throws: JMSException
 */

public class MqCustomer {
    public static final String URL = "tcp://192.168.217.130:61616";

    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, URL);
        Connection connection = null;
        Session session = null;
        Destination destination = null;

        MessageConsumer messageConsumer = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("Object-up");

            messageConsumer = session.createConsumer(destination);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long time = null;
            String dateStr = "";

            while (true) {
                TextMessage textMessage = (TextMessage) messageConsumer.receive();
                if (null != textMessage) {
                    time = new Long(System.currentTimeMillis());
                    dateStr = format.format(time);
                    System.out.println("消费消息为->" + textMessage.getText() + "\n" + "消息总线Id->" + textMessage.getJMSMessageID() + "消费时间->" + dateStr);
                } else {
                    break;
                }

            }

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
