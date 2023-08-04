package com.dtu.clientside.controller;

import com.dtu.shared.Config;
import com.dtu.shared.controller.GameController;
import com.dtu.shared.model.Board;
import com.dtu.shared.model.Player;
import com.dtu.shared.model.Space;
import com.dtu.shared.model.fieldTypes.Checkpoint;
import com.dtu.clientside.RobotRally;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

import java.util.*;

public class ClientController {
    RobotRally robotRally;
    GameController gameController;
    UUID gameID;

    public ClientController(RobotRally robotRally) {
        this.robotRally = robotRally;
    }

    // Create a new game
    public void newGame() {
        ChoiceDialog<Integer> numberDialog = new ChoiceDialog<>(Config.PLAYER_NUMBER_OPTIONS.get(0), Config.PLAYER_NUMBER_OPTIONS);
        numberDialog.setTitle("Player number");
        numberDialog.setHeaderText("Select number of players");
        Optional<Integer> numberResult = numberDialog.showAndWait();
        var boards = HttpClientSynch.boardsNoExcept();
        ChoiceDialog<String> boardDialog = new ChoiceDialog<>(boards.get(0), boards);
        boardDialog.setTitle("Board");
        boardDialog.setHeaderText("Select board");
        Optional<String> boardResult = boardDialog.showAndWait();
        if (numberResult.isPresent() && boardResult.isPresent()) {
            gameID = HttpClientSynch.newGameNoExcept(numberResult.get(), boardResult.get());
            var board = HttpClientSynch.loadGameNoExcept(gameID);
            InitializeGame(board);
        }
    }

    // Initialize the game
    private void InitializeGame(Board board) {
        List<Player> players = new ArrayList<>();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Space space = board.getSpace(x, y);
                if (space.getPlayer() != null){
                    players.add(space.getPlayer());
                    space.getPlayer().setSpace(space);
                }
                space.board = board;
            }
        }
        for (var player : players) {
            for (var card : player.cards) {
                card.player = player;
            }
            for (var card : player.program) {
                card.player = player;
            }
            player.setBoard(board);
        }
        board.setPlayers(players);
        board.winCon = Math.toIntExact(Arrays.stream(board.spaces)
                .flatMap(spaces -> Arrays.stream(spaces).map(space -> space.actions))
                .map(fieldActions -> fieldActions.stream()
                        .filter(fieldAction -> fieldAction instanceof Checkpoint)
                        .count())
                .reduce(0L, Long::sum));
        gameController = new GameController(board);
        robotRally.createBoardView(gameController);
    }

    // Save the game to the server
    public void saveGame() {
        HttpClientSynch.saveGameNoExcept(gameID, gameController.board);
    }

    // Load a game from the server
    public void loadGame() {
        var games = HttpClientSynch.listNoExcept();
        ChoiceDialog<String> choices = new ChoiceDialog<>(games.get(0), games);
        choices.setTitle("Load Board");
        choices.setHeaderText("Select a saved game to load");
        Optional<String> result = choices.showAndWait();
        var board = HttpClientSynch.loadGameNoExcept(UUID.fromString(result.get()));
        InitializeGame(board);
    }

    // Stop the game, giving the user the option to save the game, exit without saving or to keep playing.
    public boolean stopGame() {
        if (gameID != null) {
            ChoiceDialog<String> choices = new ChoiceDialog<>("Save Game","Don't Save", "Keep playing");
            choices.setTitle("Save game");
            choices.setTitle("Don't Save");
            choices.setHeaderText("Do you want to save and exit the game?");
            Optional<String> result = choices.showAndWait();
            if (result.get().equals("Save Game")) {
                saveGame();
                gameID = null;
                robotRally.createBoardView(null);
                return true;
            }
            if (result.get().equals("Don't Save")) {
                gameID = null;
                robotRally.createBoardView(null);
                return true;
            }
        }
        return false;
    }

    // Exit the game
    public void exit() {
        if (gameID != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Exit RobotRally?");
            alert.setContentText("Are you sure you want to exit RobotRally?");
            Optional<ButtonType> result = alert.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return;
            }
        }
        if (gameID == null || stopGame()) {
            Platform.exit();
        }
    }

    // Boolean method that returns true if a game is running, false otherwise
    public boolean isGameRunning() {
        return gameID != null;
    }
}
