package rabbitmqJava.detectApp;

public class FaceDetectUtil {
  public static String detect(String str) {
    System.out.println(">>> 얼굴 탐지중...");
    String detected = str.replace("face", "[face]");
    sleep(3000);
    return detected;
  }

  private static void sleep(long ms) {
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
