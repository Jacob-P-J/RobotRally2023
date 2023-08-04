package com.dtu.serverside.controller;

import com.dtu.Shared.Config;
import com.dtu.Shared.controller.GameController;
import com.dtu.Shared.model.Board;
import com.dtu.Shared.model.Player;
import com.dtu.Shared.model.fileaccess.InputUtil;
import com.dtu.Shared.model.fileaccess.LoadBoard;
import com.dtu.Shared.model.fileaccess.SaveGameController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
public class ServerController {

    // Ping the server to check if it is running
    @GetMapping(value = "/ping")
    public ResponseEntity<String> Ping() {
        return ResponseEntity.ok().body("Pinged");
    }

    // Create a new game with the given number of players and chosen board
    @GetMapping(value = "/newgame/{players}/{boardname}")
    public ResponseEntity<UUID> NewGame(@PathVariable int players, @PathVariable String boardname) throws IOException, URISyntaxException {
        Board board = LoadBoard.loadBoardFromFile(boardname);
        GameController gameController = new GameController(board);
        for (int i = 0; i < players; i++) {
            Player player = new Player(board, Config.PLAYER_COLORS[i], "Player " + (i + 1));
            board.addPlayer(player);
            player.setSpace(board.getSpace(i % board.width, i));
        }
        gameController.startProgrammingPhase();
        UUID id = UUID.randomUUID();
        SaveGameController.saveGameToFile(board, id.toString());
        return ResponseEntity.ok(id);
    }

    // Get the list of saved games
    @GetMapping(value = "/list")
    public ResponseEntity<List<String>> list() {
        var savedGames = SaveGameController.getSavedGames();
        return ResponseEntity.ok().body(savedGames);
    }

    // Get the list of boards
    @GetMapping(value = "/boards")
    public ResponseEntity<List<String>> boardList() {
        return ResponseEntity.ok().body(InputUtil.getBoardNames());
    }

    // Save the game to file
    @PostMapping(value = "/game/savegame/{id}")
    public ResponseEntity<UUID> saveGame(@PathVariable UUID id, @RequestBody Board board) throws IOException {
        SaveGameController.saveGameToFile(board, id.toString());
        return ResponseEntity.ok(id);
    }

    // Load the game from file
    @GetMapping(value = "/game/loadgame/{id}")
    public ResponseEntity<Board> loadGame(@PathVariable UUID id){
        var game = SaveGameController.loadGameFromFileNoExcept(id.toString());
        return ResponseEntity.ok(game);
    }

    // Delete the game file
    @DeleteMapping(value = "/game/{id}")
    public ResponseEntity<String> endGame(@PathVariable UUID id) {
        var deleted = SaveGameController.deleteGame(id.toString());
        if (!deleted){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Deleted");
    }
}
