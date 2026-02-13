package info.jab.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import io.vavr.control.Either;
import info.jab.ms.service.MyCalculatorService;
import info.jab.ms.spec.api.server.ApiApi;
import info.jab.ms.spec.model.Divide200Response;
import info.jab.ms.spec.model.SuccessResponse;
import info.jab.ms.spec.model.ErrorResponse;

@RestController
@RequestMapping("/api/v1/calculator")
public class MyCalculatorController implements ApiApi {

    private final MyCalculatorService myCalculatorService;

    public MyCalculatorController(MyCalculatorService myCalculatorService) {
        this.myCalculatorService = myCalculatorService;
    }

    //Not used
    private sealed interface ApiResponse permits ApiResponse.Success, ApiResponse.Error {
        record Success(Double data) implements ApiResponse {}
        record Error(ProblemDetail problem) implements ApiResponse {}
    }

    @Override
    @GetMapping("/divide")
    public ResponseEntity<Divide200Response> divide(
            @RequestParam(value = "a", required = true) Integer a,
            @RequestParam(value = "b", required = true) Integer b) {
        return validateParams(a, b)
            .flatMap(params -> myCalculatorService.divide(params.a(), params.b()))
            .map(this::buildSuccessResponse)
            .getOrElseGet(this::buildErrorResponse);
    }

    private record ValidatedParams(int a, int b) {}

    private Either<String, ValidatedParams> validateParams(int a, int b) {
        if (a < 0) {
            return Either.left("Parameter 'a' must be non-negative");
        }
        if (b < 0) {
            return Either.left("Parameter 'b' must be non-negative");
        }
        if (b == 0) {
            return Either.left("Division by zero is not allowed");
        }
        return Either.right(new ValidatedParams(a, b));
    }

    private ResponseEntity<Divide200Response> buildSuccessResponse(Double result) {
        SuccessResponse successResponse = new SuccessResponse().data(result);
        Divide200Response response = new Divide200Response(successResponse);
        return ResponseEntity.ok(response);
    }

    private ResponseEntity<Divide200Response> buildErrorResponse(String error) {
        ProblemDetail springProblemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            error
        );
        info.jab.ms.spec.model.ProblemDetail specProblemDetail = new info.jab.ms.spec.model.ProblemDetail()
            .status(springProblemDetail.getStatus())
            .detail(springProblemDetail.getDetail())
            .title(springProblemDetail.getTitle())
            .type(springProblemDetail.getType())
            .instance(springProblemDetail.getInstance());
        ErrorResponse errorResponse = new ErrorResponse().problem(specProblemDetail);
        Divide200Response response = new Divide200Response(errorResponse);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}
