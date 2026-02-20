package kz.bitlab.trelloG142.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {
    @GetMapping("/index")
    public ResponseEntity<String> example() {
        return ResponseEntity.status(HttpStatus.OK).body("hello");
    }
}
