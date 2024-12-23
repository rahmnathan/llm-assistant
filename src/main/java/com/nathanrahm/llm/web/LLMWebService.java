package com.nathanrahm.llm.web;

import com.nathanrahm.llm.service.LLMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/")
@RequiredArgsConstructor
public class LLMWebService {
    private final LLMService llmService;

    @PutMapping("/load")
    public void load(String document) {
        log.info("Loading a document of {} characters", document.length());
        llmService.addDocument(document);
    }

    @GetMapping("/query")
    public String query(@RequestParam String document) {
        log.info("Querying for {}", document);
        return llmService.query(document);
    }
}
