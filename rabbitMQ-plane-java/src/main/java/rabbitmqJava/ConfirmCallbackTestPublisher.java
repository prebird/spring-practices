package rabbitmqJava;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ConfirmCallbackTestPublisher {
  private static final String EXCHANGE_NAME = "hello-exchange";
  private static final String QUEUE_NAME = "confirm-queue";
  private static final String ROUTING_KEY = "confirm-routing_key";

  public static void main(String[] args) {
    try {
      System.out.println("ReliabilityTestPublisher start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");

      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
//        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
//        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        /**
         * ConfirmCallback 설정
         *
         */
        ConfirmCallback confirmCallback = (deliveryTag, multiple) -> {
          System.out.println("## ACK 수신");
          System.out.println("## Message confirmed: deliveryTag=" + deliveryTag + ", multiple=" + multiple);
        };

        ConfirmCallback nackCallback = (deliveryTag, multiple) -> {
          System.out.println("## NACK 수신");
          System.out.println("## Message not confirmed: deliveryTag=" + deliveryTag + ", multiple=" + multiple);
        };

        channel.addConfirmListener(confirmCallback, nackCallback);
        channel.confirmSelect();  // Confirm 모드 활성화

        /**
         * 메세지 발행
         */
        String message = "HELLO WORLD";
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, false ,null, message.getBytes());
        System.out.println(">> Message Sent : " + message);

        /**
         * 초 대기 -> ACK/NACK 을 받기 전까지 대기
         */
        Thread.sleep(200000);
      }
    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }

}
