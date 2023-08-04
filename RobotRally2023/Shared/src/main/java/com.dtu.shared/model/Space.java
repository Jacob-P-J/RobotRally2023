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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */

public class Space extends Subject implements Serializable {
    @Expose
    public Player player;
    @Expose
    // List of walls in the space
    public final List<Heading> walls = new ArrayList<>();
    @Expose
    // List of possible actions in the space
    public final List<FieldAction> actions = new ArrayList<>();
    @Expose(serialize = false)
    public transient Board board;
    @Expose
    public final int x;
    @Expose
    public final int y;

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
    }

    // Getters
    public Board getBoard() {
        return board;
    }
    public Player getPlayer() {
        return player;
    }
    public List<Heading> getWalls() {
        return walls;
    }
    public List<FieldAction> getActions() {
        return actions;
    }

    // Setters
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer && (player == null || board == player.getBoard())) {
            this.player = player;
            if (oldPlayer != null) {
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }
    // Updates the space based on the player's actions
    void playerChanges() {
        notifyChange();
    }
}
