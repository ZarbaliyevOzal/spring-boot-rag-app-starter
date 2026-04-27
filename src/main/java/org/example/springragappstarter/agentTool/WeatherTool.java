package org.example.springragappstarter.agentTool;

import org.springframework.stereotype.Component;

@Component
public class WeatherTool implements AgentTool {

    @Override
    public String name() {
        return "get_weather";
    }

    @Override
    public String description() {
        return "Get weather information for a given location. Input should be the location name.";
    }

    @Override
    public String execute(String input) {
        // Implementation for fetching weather information
        return "Weather information for " + input;
    }

}
