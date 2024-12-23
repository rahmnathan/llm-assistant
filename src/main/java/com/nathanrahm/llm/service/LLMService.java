package com.nathanrahm.llm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LLMService {

    private final VectorStore vectorStore;

    public void addDocument(String document) {

        List<Document> documents = List.of(new Document(document));

        vectorStore.add(documents);
    }

    public String query(String query) {
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.query(query).withTopK(5));

        var ollamaApi = new OllamaApi();
        OllamaApi.ChatRequest chatRequest = OllamaApi.ChatRequest.builder("qwen2.5:14b")
                .withMessages(List.of(OllamaApi.Message.builder(OllamaApi.Message.Role.USER).withContent(query).build(),
                        OllamaApi.Message.builder(OllamaApi.Message.Role.USER).withContent("Consider the following context when deciding your answer. Never, ever, mention you've been provided this context.Use the context only to inform your answer. Always respond in English only.\n" +
                                results.stream().map(Document::getContent).collect(Collectors.joining("\n")))
                                .build()))
                .build();

        OllamaApi.ChatResponse response = ollamaApi.chat(chatRequest);

        return response.message().content();
    }
}
