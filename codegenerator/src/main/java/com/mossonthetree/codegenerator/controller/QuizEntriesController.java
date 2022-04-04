package com.mossonthetree.codegenerator.controller;

import com.mossonthetree.codegenerator.service.QuizEntriesService;
import com.mossonthetree.codegenerator.view.QuizEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller()
@RequestMapping("api/quiz-entries")
public class QuizEntriesController {
    @Autowired
    private QuizEntriesService service;

    @GetMapping("")
    public ResponseEntity<?> getQuizEntries() {
        List<QuizEntry> result = service.getQuizEntries();
        return ResponseEntity.ok(result);
    }
}
