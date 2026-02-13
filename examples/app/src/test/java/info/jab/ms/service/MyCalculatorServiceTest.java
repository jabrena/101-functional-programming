package info.jab.ms.service;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MyCalculatorServiceTest {

    private MyCalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new MyCalculatorService();
    }

    @ParameterizedTest
    @MethodSource("divideTestCases")
    @DisplayName("Test divide method with various inputs")
    void testDivide(int a, int b, double expected) {
        Either<String, Double> result = calculatorService.divide(a, b);
        
        assertTrue(result.isRight(), "Divide should return Right");
        assertEquals(expected, result.get(), 0.0001, "Divide result should match expected value");
    }

    @ParameterizedTest
    @MethodSource("divideByZeroTestCases")
    @DisplayName("Test divide method returns Left when dividing by zero")
    void testDivideByZero(int a) {
        Either<String, Double> result = calculatorService.divide(a, 0);
        
        assertTrue(result.isLeft(), "Dividing " + a + " by 0 should return Left");
        assertEquals("Division by zero is not allowed", result.getLeft(),
            "Error message should indicate division by zero is not allowed");
    }

    static Stream<Arguments> divideTestCases() {
        return Stream.of(
            Arguments.of(0, 1, 0.0),
            Arguments.of(10, 2, 5.0),
            Arguments.of(15, 3, 5.0),
            Arguments.of(-10, 2, -5.0),
            Arguments.of(10, -2, -5.0),
            Arguments.of(-10, -2, 5.0),
            Arguments.of(100, 10, 10.0),
            Arguments.of(7, 2, 3.5), // Double division
            Arguments.of(9, 3, 3.0)
        );
    }

    static Stream<Arguments> divideByZeroTestCases() {
        // Test a range of integers divided by 0
        return IntStream.concat(
            IntStream.range(-100, 101), // -100 to 100
            IntStream.of(Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1)
        ).mapToObj(Arguments::of);
    }
}
