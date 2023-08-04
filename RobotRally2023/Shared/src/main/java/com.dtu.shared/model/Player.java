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

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject implements Serializable {
    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    @Expose(serialize = false)
    public transient Board board;
    @Expose
    public String name;
    @Expose
    public String color;
    @Expose(serialize = false)
    public transient Space space;
    @Expose
    public Heading heading = Heading.SOUTH;
    @Expose
    public CommandCardField[] program;
    @Expose
    public CommandCardField[] cards;
    @Expose
    public int checkpointProgression = 0;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    // Getters
    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public Board getBoard() {
        return board;
    }
    public Space getSpace() {
        return space;
    }
    public Heading getHeading() {
        return heading;
    }
    public CommandCardField getProgramField(int i) {
        return program[i];
    }
    public CommandCardField getCardField(int i) {
        return cards[i];
    }
    public int getCheckpointProgression() {
        return checkpointProgression;
    }

    // Setters
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanges();
            }
        }
    }
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanges();
        }
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.getBoard() == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanges();
            }
        }
    }
    public void setProgram(CommandCardField[] program) {
        this.program = program;
    }
    public void setCards(CommandCardField[] cards) {
        this.cards = cards;
    }
    public void setCheckpointProgression(int checkpointProgression) {
        this.checkpointProgression = checkpointProgression;
    }

    // Check for win condition
    public boolean hasWon() {
        return board.getWinCon() == this.checkpointProgression;
    }
}
