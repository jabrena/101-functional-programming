package info.jab.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import info.jab.ms.service.MyCalculatorService;

@RestController
@RequestMapping("/api/v1/calculator")
public class MyCalculatorController {

    private final MyCalculatorService myCalculatorService;

    public MyCalculatorController(MyCalculatorService myCalculatorService) {
        this.myCalculatorService = myCalculatorService;
    }

    private sealed interface ApiResponse permits ApiResponse.Success, ApiResponse.Error {
        record Success(Double data) implements ApiResponse {}
        record Error(ProblemDetail problem) implements ApiResponse {}
    }

    @GetMapping("/divide")
    public ResponseEntity<ApiResponse> divide(@RequestParam int a, @RequestParam int b) {
        return myCalculatorService.divide(a, b)
            .map(this::buildSuccessResponse)
            .getOrElseGet(this::buildErrorResponse);
    }

    private ResponseEntity<ApiResponse> buildSuccessResponse(Double result) {
        return ResponseEntity.ok(new ApiResponse.Success(result));
    }

    private ResponseEntity<ApiResponse> buildErrorResponse(String error) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            error
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse.Error(problemDetail));
    }
}
