package com.example.servico;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiServico {

   public List<EnderecoDto> getListaEnderecoDto(String nomeFilme) throws IOException, InterruptedException {
        try {
            String encodedName = URLEncoder.encode(nomeFilme, "UTF-8");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.omdbapi.com/?i=tt3896198&apikey=65c97b73&s=" + encodedName))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            String jsonResponse = response.body();
    
            ObjectMapper mapper = new ObjectMapper();
            List<EnderecoDto> filmes = new ArrayList<>();
            if (jsonResponse.contains("Search")) {
                JsonNode rootNode = mapper.readTree(jsonResponse);
                JsonNode searchResults = rootNode.get("Search");
                for (JsonNode result : searchResults) {
                    filmes.add(mapper.treeToValue(result, EnderecoDto.class));
                }
            }
            return filmes;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public EnderecoDto getEnderecoDto(String nomeFilme) throws IOException, InterruptedException {
        try {
            String encodedName = URLEncoder.encode(nomeFilme, "UTF-8");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://www.omdbapi.com/?i=tt3896198&apikey=65c97b73&t=" + encodedName))
                .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
    
            String jsonResponse = response.body();
    
            ObjectMapper mapper = new ObjectMapper();
            InputStream jsonData = new ByteArrayInputStream(jsonResponse.getBytes());
            return mapper.readValue(jsonData, EnderecoDto.class);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}