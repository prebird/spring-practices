package com.example.batchpractice.gsExample;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class PersonItemProcessor implements ItemProcessor<Person, Person> {

  @Override
  public Person process(Person person) throws Exception {
    final String firstName = person.firstName().toUpperCase();
    final String lastName = person.lastName().toUpperCase();

    final Person transformedPerson = new Person(firstName, lastName);

    log.info("Converting (" + person + ") into (" + transformedPerson + ")");

    return transformedPerson;
  }
}
