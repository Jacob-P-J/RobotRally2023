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
package com.dtu.shared.model;

import com.dtu.shared.observer.Subject;
import com.google.gson.annotations.Expose;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

public class Board extends Subject implements Serializable {
    @Expose
    public int width;
    @Expose
    public int height;
    @Expose
    public Space[][] spaces;
    @Expose(serialize = false)
    public List<Player> players = new ArrayList<>();
    @Expose
    public Player current;
    @Expose
    public Phase phase = Phase.INITIALISATION;
    @Expose
    public int step = 0;
    @Expose
    public boolean stepMode;
    @Expose
    public int winCon;
    @Expose
    public Player winner;

    public Board(int width, int height){
        this(width, height, 0);
    }
    public Board(int width, int height, int wincon) {
        this.width = width;
        this.height = height;
        this.winCon = wincon;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    // Getters
    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }
    public Space[][] getSpaces() {return spaces;}
    public List<Player> getPlayers() {return players;}
    public int getPlayersNumber() {return players.size();}
    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }
    public Player getCurrentPlayer() {return current;}
    public int getPlayerNumber(@NotNull Player player) {
        if (player.getBoard() == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }
    public Phase getPhase() {return phase;}
    public int getStep() {return step;}
    public String getStatusMessage() {
        // Shows the current phase, player and step
        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", Step: " + getStep();
    }
    public Space getAdjacentSpace(@NotNull Space space, @NotNull Heading heading) {
        if (space.getWalls().contains(heading)) {
            // Return null if there is a wall in the way
            return null;
        }
        int x = space.x;
        int y = space.y;
        // Finds the space in the direction of the heading
        switch (heading) {
            case SOUTH -> y = (y + 1) % height;
            case WEST -> x = (x + width - 1) % width;
            case NORTH -> y = (y + height - 1) % height;
            case EAST -> x = (x + 1) % width;
        }
        Heading inverse = Heading.values()[(heading.ordinal() + 2) % Heading.values().length];
        Space adjacent = getSpace(x, y);
        // Checks if the space has a wall in the inverse direction
        if (adjacent != null) {
            if (adjacent.getWalls().contains(inverse)) {
                return null;
            }
        }
        return adjacent;
    }
    public int getWinCon() {return winCon;}
    public Player getWinner() {return winner;}

    // Setters
    public void setPlayers(List<Player> players) {this.players = players;}
    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }
    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }
    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }
    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }
    public void setWinner(Player winner) {
        if(this.winner == null && winner.hasWon())
            this.winner = winner;
        notifyChange();
    }

    // Add a player to the game
    public void addPlayer(@NotNull Player player) {
        if (current == null) current = player;
        if (player.getBoard() == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    // Check for step mode
    public boolean isStepMode() {
        return stepMode;
    }
}
