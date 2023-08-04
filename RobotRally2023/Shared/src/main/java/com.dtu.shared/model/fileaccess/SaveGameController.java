package com.dtu.shared.model.fileaccess;

import com.dtu.shared.Config;
import com.dtu.shared.model.Board;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;

public class SaveGameController {
    private static final Path appData = Path.of(System.getenv("APPDATA"),"Robotrally", Config.GAMESFOLDER);

    // Save the game to file
    public static void saveGameToFile(Board board, String fileName) throws IOException {
        var file = Path.of(appData.toString(), fileName).toFile();
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(board);
        objectOutputStream.close();
    }

    // Get the list of saved games
    public static ArrayList<String> getSavedGames() {
        File[] savedGames = new File(String.valueOf(appData)).listFiles();
        ArrayList<String> savedGamesList = new ArrayList<>();
        for (File savedGame : savedGames) {
            String name = savedGame.getName();
            savedGamesList.add(name);
        }
        return savedGamesList;
    }

    // Load the game from file
    public static Board loadGameFromFile(String fileName) throws IOException, ClassNotFoundException {
        var file = Path.of(appData.toString(), fileName).toFile();
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Board board = (Board) objectInputStream.readObject();
        objectInputStream.close();
        return board;
    }

    // Load the game from file without exception
    public static Board loadGameFromFileNoExcept(String fileName){
        try {
            return loadGameFromFile(fileName);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete the game file
    public static boolean deleteGame(String fileName) {
        var file = Path.of(appData.toString(), fileName).toFile();
        return file.delete();
    }
}
