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
package com.dtu.clientside;

import com.dtu.clientside.controller.ClientController;
import com.dtu.shared.controller.IGameController;
import com.dtu.clientside.view.BoardView;
import com.dtu.clientside.view.RobotRallyMenuBar;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
// This the clientside main class, which is the entry point for the game
public class RobotRally extends Application {
    private static final int MIN_APP_WIDTH = 600;
    private Stage stage;
    public BorderPane boardRoot;

    // Initializes the application
    @Override
    public void init() throws Exception {
        super.init();
    }


    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        // create and set the scene graph, initially empty
        ClientController clientController = new ClientController(this);
        RobotRallyMenuBar menuBar = new RobotRallyMenuBar(clientController);
        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        Scene primaryScene = new Scene(vbox);
        stage.setScene(primaryScene);
        stage.setTitle("RobotRally");
        stage.setOnCloseRequest(
                e -> {
                    e.consume();
                    clientController.exit();
                } );
        stage.setResizable(false);
        stage.sizeToScene();
        stage.show();
    }

    // Creates the board view
    public void createBoardView(IGameController gameController) {
        // Clears the old board if any
        boardRoot.getChildren().clear();

        // Adds the new board
        if (gameController != null) {
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
        stage.sizeToScene();
    }

    // Stops the application
    @Override
    public void stop() throws Exception {
        super.stop();
    }

    // Game launcher
    public static void main(String[] args) {
        launch(args);
    }
}