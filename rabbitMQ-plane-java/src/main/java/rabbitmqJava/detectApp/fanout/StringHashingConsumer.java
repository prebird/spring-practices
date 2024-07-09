package rabbitmqJava.detectApp.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.util.HashMap;
import java.util.Map;

/**
 * 입력받은 문자열을 해시로 변환하여 저장합니다.
 * 1. 중복된 스팸 요청을 차단하려는 목적
 * 2. 모든 요청을 fanout 방식으로 같이 전달 받는다.
 */
public class StringHashingConsumer {
  static Map<Integer, Integer> hashAndCountMap = new HashMap<>();

  public static void main(String[] args) {
    try {
      System.out.println("StringHashingConsumer start");

      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri("amqp://localhost");
      connectionFactory.setPort(5672);
      connectionFactory.setUsername("username");
      connectionFactory.setPassword("password");
      connectionFactory.setHost("localhost");
      try (Connection connection = connectionFactory.newConnection()) {
        try (Channel channel = connection.createChannel()) {
          // 큐 생성
          String hashQueueName = "hashing-consumer-" + ProcessHandle.current().pid();
          boolean durable = false; // 메시지가 내구성을 갖지 않음
          boolean exclusive = false; // 큐가 연결에 전용되지 않음
          boolean autoDelete = true; // 마지막 소비자가 연결을 끊을 때 큐가 자동으로 삭제됨
          channel.queueDeclare(hashQueueName, durable,exclusive,autoDelete, null);
          // 큐 바인딩
          channel.queueBind(hashQueueName, "fanout-rpc-requests", "");  // // fanout 방식에서 Routing key 는 무시됨

          DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received message: " + message);

            // 입력값 해쉬 코드 변환
            int inputHash = message.hashCode();
            System.out.println("inputHash: " + inputHash);
            // 해쉬값 빈도 체크
            Integer count = hashAndCountMap.getOrDefault(inputHash, 0);
            count += 1;
            hashAndCountMap.put(inputHash, count);
            if (count > 1) {
              System.out.println("message : " + message + "is Duplicated");
              boolean requeue = false;
              channel.basicReject(delivery.getEnvelope().getDeliveryTag(), requeue);
            } else {
              System.out.println("hash saved");
            }
          };
          // 취소 콜백 정의
          CancelCallback cancelCallback = consumerTag -> {
            System.out.println("Consumer cancelled: " + consumerTag);
          };

          // 기본 소비자 설정 (autoAck를 false로 설정하여 수동 확인 사용)
          boolean autoAck = false;
          channel.basicConsume(hashQueueName, autoAck, deliverCallback, cancelCallback);


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
