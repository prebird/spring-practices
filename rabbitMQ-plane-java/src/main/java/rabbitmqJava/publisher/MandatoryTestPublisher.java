package rabbitmqJava.publisher;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ReturnCallback;

public class MandatoryTestPublisher {
  private static final String EXCHANGE_NAME = "hello-exchange";
  private static final String QUEUE_NAME = "hello-queue";
  private static final String ROUTING_KEY = "hello-routing_key";

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
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        /**
         * Basic.Return 시 동작할 callback 을 작성합니다.
         */
        ReturnCallback returnCallback = (returnValue) -> {
          System.out.println("## returnCallback");
          System.out.println("Reply code: " + returnValue.getReplyCode());
          System.out.println("Reply text: " + returnValue.getReplyText());
          System.out.println("Returned message: " + returnValue.getRoutingKey());
          System.out.println("Returned message: " + returnValue.getBody());
        };
        channel.addReturnListener(returnCallback);

        /**
         * mandatory true 로 설정하여 발행합니다.
         * MQ는 메세지를 라우팅 할 수 없는 경우 Basic.Return RPC 명령을 반환합니다.
         * Basic.Return 은 valid exchange + invalid routing key 의 경우에 발생합니다.
         */
        boolean mandatory = true;
        String message = "HELLO WORLD";
        channel.basicPublish(EXCHANGE_NAME, "not_exists_key",mandatory ,null, message.getBytes());
        System.out.println(">> Message Sent : " + message);

        /**
         * 5초 대기 -> Basic.Return 을 받기 전까지 대기
         * RabbitMQ 3.3 버전 이후 mandatory 동작이 비동기로 변경되었다.
         * 해당 패치로 publish 성능이 많이 개선되었다고 한다. 하지만, 응답을 받기 전에 어플리케이션이 종료되면 응답을 받지 못한다.
         * 응답을 받을 때 까지 어플리케이션이 살아있어야 하므로, 5초간 대기한다.
         * https://www.rabbitmq.com/blog/2014/04/03/an-end-to-synchrony-performance-improvements-in-3-3#mandatory-publishing
         */
        Thread.sleep(5000);
      }
    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}
