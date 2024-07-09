package rabbitmqJava.detectApp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicLong;

public class FaceDetectPublisher {
  static Scanner scanner = new Scanner(System.in);
  static AtomicLong strId = new AtomicLong(0);
  static Map<Long, String> inputs = new HashMap<>();

  public static void main(String[] args) {
    try {
      System.out.println("FaceDetectPublisher start");
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");
      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {
          // 응답 받을 큐 선언
          boolean durable = false; // 메시지가 내구성을 갖지 않음
          boolean exclusive = false; // 큐가 연결에 전용되지 않음
          boolean autoDelete = true; // 마지막 소비자가 연결을 끊을 때 큐가 자동으로 삭제됨
          String queueName = "detect-publisher-" + ProcessHandle.current().pid();
          channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
          System.out.println(queueName + " queue declared");

          // routing key 를 큐 이름으로 바인딩
          channel.queueBind(queueName, "rpc-replies", queueName);


          while (true) {
            // 문자열 입력 받기
            String input = scanner.nextLine();
            System.out.println("input: " + input);
            long id = strId.addAndGet(1L);
            inputs.put(id, input);

            AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .appId("Face Detect Worker(Consumer)")
                .correlationId(Long.toString(id)) // 문자열 ID
                .replyTo(queueName) // 응답 받을 큐 이름
                .build();

            // 발행
            System.out.println("published");
            channel.basicPublish("direct-rpc-requests", "detect-faces", props, input.getBytes());

            // 처리 응답 수신
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
              String message = new String(delivery.getBody(), "UTF-8");
              System.out.println("processed Result: " + message);
              channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            };
            // 취소 콜백 정의
            CancelCallback cancelCallback = consumerTag -> {
              System.out.println("Consumer cancelled: " + consumerTag);
            };

            // 기본 소비자 설정 (autoAck를 false로 설정하여 수동 확인 사용)
            boolean autoAck = false;
            channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallback);
          }
        }
      }

    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}


