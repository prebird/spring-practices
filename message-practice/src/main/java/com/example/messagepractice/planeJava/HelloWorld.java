package com.example.messagepractice.planeJava;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloWorld {
  private static final String QUEUE_NAME = "JavaHelloWorldQueue";

  public static void main(String[] args) {
    ConnectionFactory connectionFactory = new ConnectionFactory();
    connectionFactory.setHost("localhost");
    try (Connection connection = connectionFactory.newConnection()) {
      Channel channel = connection.createChannel();
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
      String message = "HELLO WORLD";
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
      System.out.println(">> Message Sent : " + message);
    } catch (IOException e) {

    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }
  }
}
