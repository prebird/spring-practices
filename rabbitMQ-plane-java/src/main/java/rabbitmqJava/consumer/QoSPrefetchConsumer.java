package rabbitmqJava.consumer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class QoSPrefetchConsumer {
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
       * QoS 를 통한 소비자 프리패치 제어
       * QoS는 소비자가 소비할 수 있는 메세지 수를 제어하는 데 사용됩니다. (무거운 작업 같이 소비자에게 부담이 생기는 작업을 조절)
       *
       * prefetch size 를 1로 설정하고, autoAck 이 false 인 경우, 명시적으로 ACK 하지 않는 이상,
       * 메세지는 한건만 처리되고 ACK을 호출할 때 까지 다음 메세지를 가져 오지 않습니다.
       *
       * prefetch size 를 설정하지 않는 경우, 무한대로 간주하여 ACK을 호출하지 않더라도
       * 계속해서 메세지를 가져오게 됩니다.
       */
      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        // 프리패치 사이즈 설정
        int prefetchSize = 1;
        channel.basicQos(prefetchSize);

        // 메세지 배달 성공시 콜백
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
          String message = new String(delivery.getBody(), "UTF-8");
          System.out.println("Received message: " + message);
          // 3초간 대기
          try {
            Thread.sleep(3000);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          System.out.println("finished");
          // 수신확인 -> 하지 않으면 메시지는 도착하나, unack 상태로 큐에 남아있게된다.
//          channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
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
