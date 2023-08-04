/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package src.main.java.com.dtu.shared.model.fileaccess;

import src.main.java.com.dtu.shared.Config;
import com.dtu.shared.model.Board;
import src.main.java.com.dtu.shared.model.FieldAction;
import src.main.java.com.dtu.shared.model.Player;
import src.main.java.com.dtu.shared.model.Space;
import com.dtu.shared.model.fileaccess.Templates.BoardTemplate;
import com.dtu.shared.model.fileaccess.Templates.PlayerTemplate;
import com.dtu.shared.model.fileaccess.Templates.SpaceTemplate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard {
    private static final String DEFAULTBOARD = "defaultboard";

    // Loads a game from a file
    public static Board loadBoardFromFile(String boardname) throws URISyntaxException, IOException {
        if (boardname == null) {boardname = DEFAULTBOARD;}
        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        String finalBoardName = boardname;
        var path = Files.list(Paths.get(classLoader.getResource(Config.BOARDSFOLDER).toURI())).filter(file -> file.getFileName().toString().startsWith(finalBoardName)).findFirst();
        if (path.isEmpty()) {
            return new Board(Config.DEFAULT_BOARD_WIDTH,Config.DEFAULT_BOARD_HEIGHT);
        }
        InputStream inputStream = Files.newInputStream(path.get());
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(Player.class, new Adapter<Player>()).
                registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter())
                .registerTypeAdapter(Board.class, new Adapter<Board>());
        Gson gson = simpleBuilder.create();
		Board foundBoard;
        JsonReader reader = null;
		try {
            // Loads the board from the file
			reader = gson.newJsonReader(new InputStreamReader(inputStream));
			BoardTemplate boardtemplate = gson.fromJson(reader, BoardTemplate.class);
			foundBoard = new Board(boardtemplate.width, boardtemplate.height);
            // Loads and adds the players to the board
            for (PlayerTemplate playerTemplate: boardtemplate.players) {
                Player player = new Player(foundBoard, playerTemplate.color, playerTemplate.name);
                player.setSpace(foundBoard.getSpace(playerTemplate.x, playerTemplate.y));
                foundBoard.addPlayer(player);
            }
            // Loads spaces of the board
			for (SpaceTemplate spaceTemplate: boardtemplate.spaces) {
			    Space space = foundBoard.getSpace(spaceTemplate.x, spaceTemplate.y);
			    if (space != null) {
                    space.getActions().addAll(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                }
            }
			reader.close();
			return foundBoard;
		} catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e2) {}
			}
		}
		return null;
    }

    // Loads a game board from a json string
    public static Board loadBoard(String jsonBoard){
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());
        Gson gson = simpleBuilder.create();
        return gson.fromJson(jsonBoard, Board.class);
    }
}
