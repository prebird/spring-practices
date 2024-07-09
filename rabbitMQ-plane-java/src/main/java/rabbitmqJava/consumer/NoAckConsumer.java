package rabbitmqJava.consumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class NoAckConsumer {
  private static final String QUEUE_NAME = "hello-queue";
  private static final String ROUTING_KEY = "hello-routing_key";

  public static void main(String[] args) {
    System.out.println("consumer started");
    try {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");

      /**
       * No Ack Consumer
       * 메세지 수신 후 Basic.Ack 응답을 하지 않아도 수신 처리를 한다.
       * 일회용 메세지에 주로 사용된다.
       */
      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        // 메세지 배달 성공시 콜백
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println("Received message: " + message);
        };

        // 취소 콜백 정의
        CancelCallback cancelCallback = consumerTag -> {
          System.out.println("Consumer cancelled: " + consumerTag);
        };

        // 기본 소비자 설정 (autoAck를 true로 no_ack 컨슈머 설정)
        boolean autoAck = true;
        channel.basicConsume(QUEUE_NAME, autoAck, deliverCallback, cancelCallback);

        // 프로그램이 종료되지 않도록 무한 루프
        while (true) {
          Thread.sleep(100);
        }
      }

    }  catch (Exception e) {
      System.out.println("## Exception!");
      System.out.println(e.getCause());
    }
  }
}
