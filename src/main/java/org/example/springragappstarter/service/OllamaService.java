package org.example.springragappstarter.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OllamaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseUrl = "http://localhost:11434";

    /**
     * Get embeddings for the given text using the Ollama API.
     * @param text The input text to get embeddings for.
     * @return A list of doubles representing the embeddings for the input text.
     */
    public List<Double> embed(String text) {
        String url = baseUrl + "/api/embeddings";

        Map<String, Object> body = new HashMap<>();
        body.put("model", "nomic-embed-text");
        body.put("prompt", text);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        return (List<Double>) response.getBody().get("embedding");
    }

    /**
     * Generate text using the Ollama API.
     * @param prompt The input prompt to generate text from.
     * @return The generated text response from the Ollama API.
     */
    public String generate(String prompt) {
        String url = baseUrl + "/api/generate";

        Map<String, Object> body = new HashMap<>();
        body.put("model", "mistral");
        body.put("prompt", prompt);
        body.put("stream", false);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);

        return (String) response.getBody().get("response");
    }
}
