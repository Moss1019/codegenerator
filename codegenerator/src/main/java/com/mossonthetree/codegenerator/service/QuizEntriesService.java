package com.mossonthetree.codegenerator.service;

import com.mossonthetree.codegenerator.view.QuizEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizEntriesService {

    public List<QuizEntry> getQuizEntries() {
        List<QuizEntry> result = new ArrayList<>();

        result.add(new QuizEntry("man", "mannen", "de", "man"));
        result.add(new QuizEntry("vrouw", "vrouwen", "de", "woman"));
        result.add(new QuizEntry("kind", "kinderen", "het", "child"));
        result.add(new QuizEntry("auto", "autos", "de", "car"));

        return result;
    }
}
