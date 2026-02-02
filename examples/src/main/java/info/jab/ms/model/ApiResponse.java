package info.jab.ms.model;

import org.springframework.http.ProblemDetail;

public sealed interface ApiResponse<T> permits ApiResponse.Success, ApiResponse.Error {
    record Success<T>(T data) implements ApiResponse<T> {}
    record Error<T>(ProblemDetail problem) implements ApiResponse<T> {}
}
