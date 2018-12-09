package com.em248.controller;

import com.em248.generator.dto.GeneratorEntity;
import com.em248.service.GeneratorService;
import com.em248.service.dto.CreateGeneratorServerEntityRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/")
@Slf4j
public class GeneratorController {

    private GeneratorService generatorService;

    public GeneratorController(GeneratorService generatorService) {
        this.generatorService = generatorService;
    }

    @GetMapping
    public String index() {
        return "index.html";
    }

    @PostMapping("/api/entity")
    @ResponseBody
    public ResponseEntity<?> generatorEntity(@Valid @RequestBody CreateGeneratorServerEntityRequest request) {
        log.info("create content: {}",request );
        generatorService.generatorServerEntity(request);
        return ResponseEntity.ok("创建成功");
    }

}
