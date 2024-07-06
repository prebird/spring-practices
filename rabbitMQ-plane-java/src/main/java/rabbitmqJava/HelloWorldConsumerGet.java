package rabbitmqJava;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

public class HelloWorldConsumerGet {
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

      try (Connection connection = connectionFactory.newConnection()) {
        Channel channel = connection.createChannel();
        /**
         * 메세지를 수신합니다.
         * 반복적으로 basicGet 을 호출합니다.
         */
        boolean autoAck = false;    // true로 설정하면 메세지를 가져오자마자 자동으로 ack 처리하기 때문에 메세지 유실의 위험이 존재
        while (true) {
          GetResponse getResponse = channel.basicGet(QUEUE_NAME, autoAck);
          if (getResponse == null) {
            System.out.println("큐가 비어 있습니다.");
          } else {
            // 메시지를 처리
            String message = new String(getResponse.getBody(), "UTF-8");
            System.out.println("Received message: " + message);
            // 메시지 확인 처리 deliveryTag 와 함께 전송합니다.
            // multiple flag: false일 경우 해당 메세지만 확인 처리합니다. true인 경우 해당 태그 이전 메세지 까지 모두 확인 처리합니다.
            channel.basicAck(getResponse.getEnvelope().getDeliveryTag(), false);
            break;  // 받을 때 까지
          }
          Thread.sleep(5000); // 5초 주가
        }
      }

    }  catch (Exception e) {
      System.out.println("## Exception!");
      System.out.println(e.getCause());
    }
  }

}
