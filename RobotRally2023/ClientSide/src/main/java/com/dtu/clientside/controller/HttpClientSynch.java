package com.dtu.clientside.controller;

import com.dtu.Shared.model.Board;
import com.dtu.Shared.model.FieldAction;
import com.dtu.Shared.model.fileaccess.FieldActionTypeAdapter;
import com.dtu.Shared.model.fileaccess.LoadBoard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class HttpClientSynch {
    public static final String BaseURI = "http://localhost:8080";
    public static UUID gameId;

    // HttpClient
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    // Starts a new game
    public static UUID newGame(int players, String board) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/newgame/" + players + "/" + board))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        gameId = UUID.fromString(body.replace("\"", ""));
        return gameId;
    }

    // Starts a new game without throwing exception
    public static UUID newGameNoExcept(int players, String board) {
        try {
            return newGame(players, board);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // list games
    public static List<String> list() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/list"))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        return Arrays.stream(new Gson().fromJson(body, String[].class)).toList();
    }

    // list games without throwing exception
    public static List<String> listNoExcept() {
        try {
            return list();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // List game boards
    public static List<String> boards() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/boards"))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        return Arrays.stream(new Gson().fromJson(body, String[].class)).toList();
    }

    // List game boards without throwing exception
    public static List<String> boardsNoExcept() {
        try {
            return boards();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Save the current game
    public static boolean saveGame(UUID id, Board board) throws IOException, InterruptedException {
        GsonBuilder simpleBuilder = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());
        Gson gson = simpleBuilder.create();
        System.out.println(gson.toJson(board));
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(board)))
                .uri(URI.create(BaseURI + "/game/savegame/" + id))
                .setHeader("Content-Type", "application/json")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }

    // Save game without throwing exception
    public static boolean saveGameNoExcept(UUID id, Board board) {
        try {
            return saveGame(id, board);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            throw e;
        }
    }

    // Load saved game
    public static Board loadGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BaseURI + "/game/loadgame/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return LoadBoard.loadBoard(body);
    }

    // Load saved game without throwing exception
    public static Board loadGameNoExcept(UUID id) {
        try {
            return loadGame(id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete saved game
    public static boolean deleteGame(UUID id) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(BaseURI + "/game/" + id))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var body = response.body();
        System.out.println(body);
        return response.statusCode() == 200;
    }

    // Delete saved game without throwing exception
    public static boolean deleteGameNoExcept(UUID id) {
        try {
            return deleteGame(id);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
