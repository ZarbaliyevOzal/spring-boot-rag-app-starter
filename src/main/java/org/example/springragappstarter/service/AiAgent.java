package org.example.springragappstarter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.startup.Tool;
import org.example.springragappstarter.agentTool.AgentTool;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class AiAgent {

    private final OllamaService ollamaService;
    private final List<AgentTool> tools;

    public AiAgent(OllamaService ollamaService, List<AgentTool> tools) {
        this.ollamaService = ollamaService;
        this.tools = tools;
    }

    public String handle(String userInput) {
        System.out.println("Asking AI Agent with input: " + userInput);

        String toolDescriptions = tools.stream()
            .map(tool -> tool.name() + ": " + tool.description())
            .collect(Collectors.joining("\n"));

        String prompt = """
        You are an AI agent.
        
        You have access to the following tools:

        %s

        If needed, respond in JSON:
        {
            "tool": "tool_name",
            "input": "value_for_tool"
        }

        Otherwise, answer the question directly.

        User: %s
        """.formatted(
            toolDescriptions,
            userInput
        );

        String response = ollamaService.generate(prompt);

        System.out.println("AI Agent response: " + response);

        return executeTool(response);
    }

    public String executeTool(String response) {
        try {
            System.out.println("AI Agent wants to execute tool with response: " + response);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response);

            if (jsonNode.has("tool") && jsonNode.has("input")) {

                String toolName = jsonNode.get("tool").asString();
                String toolInput = jsonNode.get("input").asString();

                System.out.println("AI Agent wants to execute tool: " + toolName + " with input: " + toolInput);

                AgentTool tool = tools.stream()
                    .filter(t -> t.name().equals(toolName))
                    .findFirst()
                    .orElseThrow();

                String result = tool.execute(toolInput);

                System.out.println("Tool result: " + result);
                return ollamaService.generate("Tool result: " + result+ "\nRespond to the user with this information.");
            }
        } catch (Exception ignored) {}

        return response;
    }

}
