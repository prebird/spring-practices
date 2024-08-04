package org.prebird.springpractice.proxyFactory;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prebird.springpractice.common.ConcreteClass;
import org.prebird.springpractice.common.ServiceImpl;
import org.prebird.springpractice.common.ServiceInterface;
import org.springframework.aop.framework.ProxyFactory;

@Slf4j
public class ProxyFactoryTest {

  @Test
  @DisplayName("인터페이스가 있으면 JDK 동적 프록시")
  public void interfaceProxy() {
    ServiceInterface target = new ServiceImpl();

    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.addAdvice(new TimeAdvice());   // 프록시 로직
    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

    log.info("target class = {}", target.getClass().getSimpleName());
    log.info("proxy class = {}", proxy.getClass().getSimpleName());

    proxy.save();
  }

  @Test
  @DisplayName("구체클래스가 있으면 CGLIB 프록시")
  public void concreteProxy() {
    ConcreteClass target = new ConcreteClass();

    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.addAdvice(new TimeAdvice());   // 프록시 로직
    ConcreteClass proxy = (ConcreteClass) proxyFactory.getProxy();

    log.info("target class = {}", target.getClass().getSimpleName());
    log.info("proxy class = {}", proxy.getClass().getSimpleName());

    proxy.save();
  }

}
