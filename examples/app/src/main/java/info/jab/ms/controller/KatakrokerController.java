package info.jab.ms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

@RestController 
@RequestMapping("/api/v1")
public class KatakrokerController {
    
    @GetMapping("/katakroker")
    public ResponseEntity<String> getKatacroker() {
        throw new RuntimeException("Katacroker is not available");
    }
}
