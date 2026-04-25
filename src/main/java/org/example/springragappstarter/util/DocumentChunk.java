package org.example.springragappstarter.util;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DocumentChunk {
    public String text;
    public List<Double> embedding;
}
