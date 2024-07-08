package rabbitmqJava;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.GetResponse;

public class HelloWorldConsumerConsume {
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
       * Basic.Consume
       * 비동기로 동작하기 때문에 성공, 실패 callback 을 등록해 주어야한다.
       * 수신 확인 시, basic.ack 요청을 하는데, delivery tag 를 전송해주어 메세지를 특정하도록 해야한다.
       */
      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        // 메세지 배달 성공시 콜백
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println("Received message: " + message);
          // 수신확인
          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        // 취소 콜백 정의
        CancelCallback cancelCallback = consumerTag -> {
          System.out.println("Consumer cancelled: " + consumerTag);
        };

        // 기본 소비자 설정 (autoAck를 false로 설정하여 수동 확인 사용)
        boolean autoAck = false;
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
