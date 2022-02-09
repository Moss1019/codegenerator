package com.mossonthetree.codegenerator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class StatusController {
  @GetMapping("")
  public ResponseEntity<?> getStatus() {
    return ResponseEntity.ok("up");
  }
}
