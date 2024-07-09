package rabbitmqJava.detectApp.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class DeclareExchange {
  private static final String[] exchanges = {"rpc-replies", "direct-rpc-requests"};

  public static void main(String[] args) {
    try {
      System.out.println("DeclareMQ start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");

      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {
          for (String exchange: exchanges) {
            channel.exchangeDeclare(exchange, "direct");
          }
        }

      }
    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }

}
