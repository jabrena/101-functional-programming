package info.jab.ms.service;

import org.springframework.stereotype.Service;

import io.vavr.control.Either;

@Service
public class MyFaultyService {
    
    public Either<String, String> process() {
        return Either.left("My Faulty Service is not available");
    }
}
