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
package com.dtu.clientside.view;

import com.dtu.clientside.controller.ClientController;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RobotRallyMenuBar extends MenuBar {

    private ClientController clientController;
    private Menu controlMenu;
    private MenuItem saveGame, newGame, loadGame, stopGame, exitApp;

    // This is the menu bar that is shown at the top of the game window
    public RobotRallyMenuBar(ClientController clientController) {
        this.clientController = clientController;

        // File menu that will contain the other menu items
        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);

        newGame = new MenuItem("New Game");
        newGame.setOnAction(e -> this.clientController.newGame());
        controlMenu.getItems().add(newGame);

        stopGame = new MenuItem("Stop Game");
        stopGame.setOnAction(e -> this.clientController.stopGame());
        controlMenu.getItems().add(stopGame);

        saveGame = new MenuItem("Save Game");
        saveGame.setOnAction(e -> this.clientController.saveGame());
        controlMenu.getItems().add(saveGame);

        loadGame = new MenuItem("Load Game");
        loadGame.setOnAction( e -> this.clientController.loadGame());
        controlMenu.getItems().add(loadGame);

        exitApp = new MenuItem("Exit");
        exitApp.setOnAction(e -> this.clientController.exit());
        controlMenu.getItems().add(exitApp);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());
        update();
    }

    // This is to update the menu bar when the game is changing between being in a game and not being in a game
    public void update() {
        if (clientController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            loadGame.setVisible(false);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
        }
    }

}
