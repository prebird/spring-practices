package org.prebird.springpractice.proxyFactory;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 프록시 로직을 구현한다.
 * 프록시 객체를 호출하지만, target 인스턴스가 보이지 않는다.
 * 그 이유는 invocation.proceed() 를 호출하면 target의 메서드가 호출되기 때문이데, 이는 생성할때 입력받아 저장되기 때문이다.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {    // aop 패키지

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    log.info("TimeProxy 실행");
    long startTime = System.currentTimeMillis();
    Object result = invocation.proceed();
    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("TimeProxy 종료 resultTime={}ms", resultTime);
    return result;
  }
}
