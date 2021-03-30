package net.kimleo.archunit.service;

import net.kimleo.archunit.repository.SomeRepository;
import org.junit.Test;

public class SomeServiceTest {
    @Test
    public void testItWorksToo() {
        SomeService service = new SomeService(new SomeRepository());

        service.doSomething();
    }
}