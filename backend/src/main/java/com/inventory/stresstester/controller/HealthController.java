package com.inventory.stresstester.controller;
import com.inventory.stresstester.dto.ApiInfoResponse;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api")
public class HealthController {
    @GetMapping("/health")
    public ApiInfoResponse health() {
        return new ApiInfoResponse("Inventory Demand Shock & Supplier Lead-Time Stress Tester", "UP",
            "Spring Boot backend is running");
    }
}
