package rabbitmqJava.stream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Collections;

public class ConsumeStream {

  public static void main(String[] args) {
    try {
      System.out.println("ConsumeStream start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");
      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {

          // QoS must be specified
          channel.basicQos(100);
          channel.basicConsume(
              "my-stream",    // 큐 이름
              false,  // autoAck
              Collections.singletonMap("x-stream-offset", "first"), // "first" offset specification
              (consumerTag, message) -> {   // 성공 콜백
                System.out.println(">> message arrived : " + message.getBody().toString());
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false); // ack is required
              },
              consumerTag -> { });  // 실패 콜백
        }
      }

    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}
