package rabbitmqJava;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.HashMap;
import java.util.Map;

public class DeadLetterExchange {
  private static final String MAIN_QUEUE_NAME = "main-queue";
  private static final String MAIN_EXCHANGE_NAME = "main-exchange";
  private static final String MAIN_ROUTING_KEY = "main-routing_key";

  private static final String DEAD_LETTER_EXCHANGE_NAME = "dead_letter_exchange";
  private static final String DEAD_LETTER_QUEUE_NAME = "dead_letter_queue";
  private static final String DEAD_LETTER_ROUTING_KEY = "dead_letter_routing_key";

  public static void main(String[] args) {
    System.out.println("consumer started");
    try {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");

      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        /**
         * Dead Letter Exchange 선언
         */
        channel.exchangeDeclare(DEAD_LETTER_EXCHANGE_NAME, "direct");
        channel.queueDeclare(DEAD_LETTER_QUEUE_NAME, false, false,false,null);
        channel.queueBind(DEAD_LETTER_QUEUE_NAME, DEAD_LETTER_EXCHANGE_NAME, DEAD_LETTER_ROUTING_KEY);

        /**
         * 메인 큐 선언
         * 메인 큐 설정에 Dead Letter Exchange 추가
         * AMQP 에서 큐는 한번 선언하면 설정을 변경할 수 없고 새로 만들어야한다.
         */
        Map<String, Object> config = new HashMap<>();
        config.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE_NAME);
        config.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);

        channel.exchangeDeclare(MAIN_EXCHANGE_NAME, "direct");
        channel.queueDeclare(MAIN_QUEUE_NAME, false, false, false, config);
        channel.queueBind(MAIN_QUEUE_NAME, MAIN_EXCHANGE_NAME, MAIN_ROUTING_KEY);

        // 테스트 메시지 발행
        String message = "Hello, RabbitMQ! DeadLetter";
        channel.basicPublish(MAIN_EXCHANGE_NAME, MAIN_ROUTING_KEY, null, message.getBytes());
        System.out.println("Sent message: " + message);

        // 테스트 메세지 소비
        // 메시지 소비자 정의
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String receivedMessage = new String(delivery.getBody(), "UTF-8");
          System.out.println("Received message: " + receivedMessage);
          /**
           * 메세지 거부 시, Dead Letter Queue 로 보내진다.
           */
          boolean requeue = false;
          boolean multiple = false;
          channel.basicNack(delivery.getEnvelope().getDeliveryTag(), multiple, requeue);
        };

        channel.basicConsume(MAIN_QUEUE_NAME, false, deliverCallback, consumerTag -> {});
        Thread.sleep(5000);
      }

    }  catch (Exception e) {
      System.out.println("## Exception!");
      System.out.println(e.getCause());
    }
  }
}
