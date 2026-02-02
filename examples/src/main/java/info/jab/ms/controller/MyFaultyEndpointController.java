package info.jab.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import info.jab.ms.model.ApiResponse;
import info.jab.ms.model.MyResponse;
import info.jab.ms.service.MyFaultyService;

@RestController
@RequestMapping("/api/v1")
public class MyFaultyEndpointController {

    private final MyFaultyService myFaultyService;

    public MyFaultyEndpointController(MyFaultyService myFaultyService) {
        this.myFaultyService = myFaultyService;
    }

    @GetMapping("/my-faulty-endpoint")
    public ResponseEntity<ApiResponse<MyResponse>> getMyFaultyEndpoint() {
        var result = myFaultyService.process();
        return result.fold(
            this::handleError,
            success -> ResponseEntity.ok(new ApiResponse.Success<>(new MyResponse(success)))
        );
    }

    private ResponseEntity<ApiResponse<MyResponse>> handleError(String error) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            error
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiResponse.Error<>(problemDetail));
    }
}
