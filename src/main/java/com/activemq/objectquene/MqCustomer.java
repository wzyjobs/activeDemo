package com.activemq.objectquene;

import com.activemq.bean.StudentDetail;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.jms.*;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @Title: activemq 对象消息队列 异步方式
 * @depression: 点对点模式
 * @author: eric
 * @date: 2018/10/22 14:39
 * @throws: JMSException
 */

public class MqCustomer {
    public static final String URL = "tcp://192.168.217.130:61616";
    public static final Object o = new Object();
    private static Logger log = Logger.getLogger(MqCustomer.class);

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream("F:\\__project\\activeDemo\\activet\\src\\main\\resources\\log4j.properties"));
        PropertyConfigurator.configure(props);


        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, URL);
        //关闭序列化安全验证
        ((ActiveMQConnectionFactory) connectionFactory).setTrustAllPackages(true);
        Connection connection = null;
        Session session = null;
        Destination destination = null;

        MessageConsumer messageConsumer = null;

        boolean flag = true;

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);

            destination = session.createQueue("Object-up");

            messageConsumer = session.createConsumer(destination);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Long time = null;
            String dateStr = "";

            messageConsumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    try {
                        StudentDetail studentdetils = (StudentDetail) ((ObjectMessage) message).getObject();
                        System.out.println(studentdetils.getName() + studentdetils.getAge());
                        log.info("receive message : " + studentdetils.getName() + studentdetils.getAge());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });

            while (flag) {//主线程阻塞
                Thread.sleep(10000 * 10000);
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
