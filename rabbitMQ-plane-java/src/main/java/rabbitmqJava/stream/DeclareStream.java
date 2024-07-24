package rabbitmqJava.stream;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.HashMap;
import java.util.Map;

public class DeclareStream {

  public static void main(String[] args) {
    try {
      System.out.println("DeclareStream start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");
      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {
          Map<String, Object> arguments = new HashMap<>();
          arguments.put("x-queue-type", "stream");
//          arguments.put("x-max-length-bytes", 20_000_000_000); // maximum stream size: 20 GB
//          arguments.put("x-stream-max-segment-size-bytes", 100_000_000); // size of segment files: 100 MB
          channel.queueDeclare(
              "my-stream",
              true,         // durable
              false, false, // not exclusive, not auto-delete
              arguments
          );
        }
      }

    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}
