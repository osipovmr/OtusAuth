package otus.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import otus.model.Response;

@RestController
public class Controller {

    @GetMapping("/health")
    public ResponseEntity<Response> helloOtus() {
        return ResponseEntity.ok(Response.builder().status("OK").build());
    }
}