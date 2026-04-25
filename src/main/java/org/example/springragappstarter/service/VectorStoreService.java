package org.example.springragappstarter.service;

import java.util.ArrayList;
import java.util.List;

import org.example.springragappstarter.util.DocumentChunk;
import org.springframework.stereotype.Service;

@Service
public class VectorStoreService {

    private final List<DocumentChunk> store = new ArrayList<>();

    public void add(DocumentChunk chunk) {
        store.add(chunk);
    }

    /**
     * Calculates the cosine similarity between two vectors.
     *
     * @param vecA the first vector
     * @param vecB the second vector
     * @return the cosine similarity
     */
    private double cosineSimilarity(List<Double> vecA, List<Double> vecB) {
        double dot = 0.0;
        double magA = 0.0;
        double magB = 0.0;

        for (int i = 0; i < vecA.size(); i++) {
            dot += vecA.get(i) * vecB.get(i);
            magA += vecA.get(i) * vecA.get(i);
            magB += vecB.get(i) * vecB.get(i);
        }

        return dot / (Math.sqrt(magA) * Math.sqrt(magB));
    }

    /**
     * Searches for the top K most similar document chunks based on cosine similarity.
     *
     * @param queryEmbedding the embedding of the query
     * @param topK the number of top results to return
     * @return a list of the top K most similar document chunks
     */
    public List<DocumentChunk> search(List<Double> queryEmbedding, int topK) {
        return store.stream()
                .sorted((a, b) -> Double.compare(
                        cosineSimilarity(b.embedding, queryEmbedding),
                        cosineSimilarity(a.embedding, queryEmbedding)))
                .limit(topK)
                .toList();
    }
}
