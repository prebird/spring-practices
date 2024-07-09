package rabbitmqJava.consumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RejectConsumer {
  private static final String QUEUE_NAME = "hello-queue";

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
       * 메제시 거부하기: Basic.Reject, Basic.Nack
       * reject - 하나의 메세지만 거부 가능 (AMQP 제공)
       * nack - 여러 메세지를 거부 가능 (RabbitMQ 확장)
       * requeue 를 true로 하면 메세지가 다시 큐로 들어간다. 아래 코드에서는 무한 루프를 돌게 된다.
       */
      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        // 메세지 배달 성공시 콜백
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println("Received message: " + message);

          boolean requeue = true;
//          channel.basicReject(delivery.getEnvelope().getDeliveryTag(), requeue);
          boolean multiple = false; // 여러 메세지 처리 여부
          channel.basicNack(delivery.getEnvelope().getDeliveryTag(), multiple, requeue);
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
