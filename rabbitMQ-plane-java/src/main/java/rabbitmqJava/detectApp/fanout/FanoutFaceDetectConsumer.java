package rabbitmqJava.detectApp.fanout;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.HashMap;
import java.util.Map;
import rabbitmqJava.detectApp.FaceDetectUtil;

public class FanoutFaceDetectConsumer {

  public static void main(String[] args) {
    try {
      System.out.println("FanoutFaceDetectConsumer start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");
      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {
          // 큐 선언 ( 자동 삭제 )
          boolean durable = false; // 메시지가 내구성을 갖지 않음
          boolean exclusive = false; // 큐가 연결에 전용되지 않음
          boolean autoDelete = true; // 마지막 소비자가 연결을 끊을 때 큐가 자동으로 삭제됨
          String queueName = "detect-consumer-" + ProcessHandle.current().pid();
          channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
          System.out.println(queueName + " queue declared");
          // 큐 바인드
          channel.queueBind(queueName, "fanout-rpc-requests", ""); // fanout 방식에서 Routing key 는 무시됨
          System.out.println("queue bind");

          // 메세지 소비자
          DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);

            // face 탐지
            String detectResult = FaceDetectUtil.detect(message);

            // Publish 된 메세지 설정 꺼내기
            AMQP.BasicProperties props = delivery.getProperties();
            // 응답 메세지 만들기
            // 응답 속성 설정
            Map<String, Object> headers = new HashMap<>();
            headers.put("first_publish", props.getTimestamp());
            AMQP.BasicProperties responseProps = new AMQP.BasicProperties.Builder()
                .appId("Face Detect Worker(Consumer)")
                .correlationId(props.getCorrelationId())  // 원본 메세지의 correlationId
                .headers(headers)
                .build();

            String replyTo = props.getReplyTo();  // 응답 전달
            System.out.println("replyTo: " + replyTo);
            // 얼굴 인식 결과를 publish (publisher에게)
            channel.basicPublish("rpc-replies", replyTo, responseProps, detectResult.getBytes());
            // 수신확인
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
          };

          // 취소 콜백 정의
          CancelCallback cancelCallback = consumerTag -> {
            System.out.println("Consumer cancelled: " + consumerTag);
          };

          // 기본 소비자 설정 (autoAck를 false로 설정하여 수동 확인 사용)
          boolean autoAck = false;
          channel.basicConsume(queueName, autoAck, deliverCallback, cancelCallback);

          // 프로그램이 종료되지 않도록 무한 루프
          while (true) {
            Thread.sleep(100);
          }


        }
      }


    } catch (Exception e) {
      System.out.println("Exception!");
      System.out.println(e.getCause());
    }
  }
}
