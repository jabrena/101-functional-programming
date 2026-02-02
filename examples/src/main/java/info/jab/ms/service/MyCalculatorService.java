package info.jab.ms.service;

import org.springframework.stereotype.Service;

import io.vavr.control.Either;

@Service
public class MyCalculatorService {

    public Either<String, Double> divide(int a, int b) {
        if (b == 0) {
            return Either.left("Division by zero is not allowed");
        }
        return Either.right((double) a / b);
    }
}
