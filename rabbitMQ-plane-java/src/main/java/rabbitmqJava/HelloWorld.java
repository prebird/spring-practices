package rabbitmqJava;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class HelloWorld {
  private static final String EXCHANGE_NAME = "hello-exchange";
  private static final String QUEUE_NAME = "hello-queue";
  private static final String ROUTING_KEY = "hello-routing_key";

  public static void main(String[] args) {
    try {
      System.out.println("main start");

      /**
       * RabbitMQ  서버와 커넥션을 생성합니다.
       * AMQP 프로토콜을 사용합니다.
       */
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");

      try (Connection connection = connectionFactory.newConnection()) {
        /**
         * channel 을 생성합니다.
         * 한번 생성된 채널은 양방향 통신이 가능합니다.
         */
        Channel channel = connection.createChannel();
        /**
         * exchange 를 생성합니다.
         * exchange Type 은 아래와 같습니다.
         * - direct: 특정 라우팅 키와 일치하는 메시지들을 전달합니다.
         * - topic: 라우팅 키의 패턴과 일치하는 메시지들을 전달합니다.
         * - fanout: 라우팅 키를 무시하고 모든 바인딩된 큐에 메시지를 전달합니다.
         * - headers: 메시지 헤더 속성에 따라 메시지를 라우팅합니다.
         */
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        /**
         * Queue 를 선언합니다.
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        /**
         * binding 을 선언합니다.
         * exchange 와 queue 를 연결합니다.
         */
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        /**
         * 메세지를 Publish 합니다.
         */
        String message = "HELLO WORLD";
        channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes());
        System.out.println(">> Message Sent : " + message);
      }
    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}
