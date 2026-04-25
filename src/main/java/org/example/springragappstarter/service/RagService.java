package org.example.springragappstarter.service;

import java.util.ArrayList;
import java.util.List;

import org.example.springragappstarter.util.DocumentChunk;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RagService {

    private final OllamaService ollamaService;
    private final VectorStoreService vectorStoreService;

    /**
     * Ingests a document by chunking it, embedding the chunks, and storing them in the vector store.
     * 
     * @param document The input document to be ingested.
     * @param chunkSize The size of each chunk to be created from the document.
     * @return A list of the ingested document chunks with their embeddings.
     */
    private List<String> chunk(String text, int size) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < text.length(); i += size) {
            chunks.add(text.substring(i, Math.min(text.length(), i + size)));
        }
        return chunks;
    }

    /**
     * Ingests a document by chunking it, embedding the chunks, and storing them in the vector store.
     *
     * @param text The input text to be ingested.
     * @void
     */
    public void ingest(String text) {
        List<String> chunks = chunk(text, 400);

        for (String chunk : chunks) {
            List<Double> embedding = ollamaService.embed(chunk);
            vectorStoreService.add(new DocumentChunk(chunk, embedding));
        }
    }

    /**
     * Answers a question by embedding the question, searching for relevant document chunks, and generating a response using the Ollama API.
     *
     * @param question The input question to be answered.
     * @return The generated answer based on the relevant document chunks.
     */
    public String ask(String question) {
        // Embed the question to get its vector representation
        List<Double> qEmbedding = ollamaService.embed(question);

        // System.out.println("Question embedding: " + qEmbedding);

        // Search for the top 3 most relevant document chunks based on cosine similarity
        var results = vectorStoreService.search(qEmbedding, 3);

        // System.out.println("Search results: " + results);

        StringBuilder context = new StringBuilder();
        for (var r : results) {
            context.append(r.text).append("\n");
        }

        String prompt = """
        Use the context below to answer the question.

        Context:
        %s

        Question:
        %s
        """.formatted(context, question);

        return ollamaService.generate(prompt);
    }

}
