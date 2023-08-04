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
package src.main.java.com.dtu.shared.controller;

import com.dtu.shared.model.*;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

// Implementation of the game controller
public class GameController implements IGameController {

    final public Board board;

    public GameController(Board board) {
        this.board = board;
    }

    // Moves the current player to the given space, if the space is empty
    public void moveCurrentPlayerToSpace (@NotNull Space space){
        Player player = board.getCurrentPlayer();
        if (space.getPlayer() == null) {
            player.setSpace(space);
        }
    }

    @Override
    public Board getBoard() {
        return board;
    }

    // Moves to a neighbouring space, if the move is possible
    public void moveToSpace(@NotNull Player player, @NotNull Space space, @NotNull Heading heading) throws ImpossibleMoveException {
        assert board.getAdjacentSpace(player.getSpace(), heading) == space; // Checks if the new space is a neighbour of the current space
        Player other = space.getPlayer();
        if (other != null) {
            Space target = board.getAdjacentSpace(space, heading);
            if (target != null) {
                // If the space is occupied, move the player on the space to the next space
                moveToSpace(other, target, heading);

                assert target.getPlayer() == null : target; // Double check that the space is empty
            } else {
                throw new ImpossibleMoveException(player, space, heading);
            }
        }
        player.setSpace(space);
    }

    public class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException(Player player, Space space, Heading heading) {
            super("Move is impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

    // Starts the programming phase
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // Generates a random command card
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // Ends the programming phase
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // Sets the registers to visible
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // Sets the registers to invisible
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // Runs the programs
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // Sets the game to step mode
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // Continues the programs
    private void continuePrograms() {
        // Executes the next step if the game is not in step mode
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
        // Executes the next card if the game is in step mode
        for (Player player:board.getPlayers()) {
            player.getSpace().actions.forEach(fieldAction -> fieldAction.doAction(this, player.getSpace()));
        }
    }

    // Executes the next step
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            // Execute the command card of the current step
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    executeCommand(currentPlayer, command);
                    // If the command is interactive, open the interactive window
                    if (command.isInteractive()){
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                }
                // Go to the next player
                getNextPlayerNumber(currentPlayer, step);
            } else {
                assert false;
            }
        } else {
            assert false;
        }
    }


    // Goes to next player
    private void getNextPlayerNumber(Player currentPlayer, int step) {
        int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
        if (nextPlayerNumber < board.getPlayersNumber()) {
            board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
        } else {
            // If it was the last player, go to the next step and the first player
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
                board.setCurrentPlayer(board.getPlayer(0));
            } else {
                // If it was the last step, go to the next programming phase
                startProgrammingPhase();
            }
        }
    }

    // Execute a command from a card
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.getBoard() == board && command != null) {
            switch (command) {
                case FORWARD:
                    this.moveForward(player, 1);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.moveForward(player,2);
                    break;
                case TRIPLE_FORWARD:
                    this.moveForward(player,3);
                    break;
                default:
            }
        }
    }

    // Moves a player forward if possible
    public void moveForward(@NotNull Player player, int amount) {
        try {
            moveDirection(player, amount, player.getHeading());
        } catch (ImpossibleMoveException e) {
            throw new RuntimeException(e);
        }
    }

    // Moves a player in the given direction if possible
    public void moveDirection(@NotNull Player player, int amount, Heading heading) throws ImpossibleMoveException {
        for (int i = 0; i < amount; i++) {
            Space space = player.getSpace();
            if (space != null) {
                Space target = board.getAdjacentSpace(space, heading);
                if (target != null) {
                    if (target.getPlayer() != null){
                        moveDirection(target.getPlayer(), 1, player.getHeading());
                    }
                    target.setPlayer(player);
                    player.setSpace(target);
                }
                if (target == null){
                    throw new ImpossibleMoveException(player, space, heading);
                }
            }
        }
    }

    // Turns a player right
    public void turnRight(@NotNull Player player) {
        if (player != null && player.getBoard() == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    // Turns a player left
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.getBoard() == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    // Lets cards be moved around
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    // Command options for the interactive window
    public void cardOption(Command option) {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.PLAYER_INTERACTION && currentPlayer != null) {
            int step = board.getStep();
            // Execute the selected command
            if (step >= 0 && step < Player.NO_REGISTERS) {
                executeCommand(currentPlayer, option);
                board.setPhase(Phase.ACTIVATION);
            }
            // Go to the next player
            getNextPlayerNumber(currentPlayer, step);
            }
        continuePrograms();
        }
}
