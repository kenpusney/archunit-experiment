package net.kimleo.archunit.service;

import lombok.RequiredArgsConstructor;
import net.kimleo.archunit.repository.SomeRepository;

@RequiredArgsConstructor
public class SomeService {
    final SomeRepository repository;


    void doSomething() {
        repository.doSomethingInRepo();
    }
}
