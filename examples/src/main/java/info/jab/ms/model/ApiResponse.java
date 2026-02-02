package info.jab.ms.model;

import org.springframework.http.ProblemDetail;

public sealed interface ApiResponse permits ApiResponse.Success, ApiResponse.Error {
    record Success(MyResponse data) implements ApiResponse {}
    record Error(ProblemDetail problem) implements ApiResponse {}
}
