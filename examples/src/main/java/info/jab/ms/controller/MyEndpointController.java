package info.jab.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

import info.jab.ms.model.MyResponse;

@RestController
@RequestMapping("/api/v1")
public class MyEndpointController {

    @GetMapping("/my-endpoint")
    public ResponseEntity<MyResponse> getMyEndpoint() {
        return ResponseEntity.ok(new MyResponse("Hello World"));
    }
}
