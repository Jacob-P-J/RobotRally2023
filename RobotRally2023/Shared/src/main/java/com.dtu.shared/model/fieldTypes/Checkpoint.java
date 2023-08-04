package src.main.java.com.dtu.shared.model.fieldTypes;

import src.main.java.com.dtu.shared.controller.IGameController;
import com.dtu.shared.model.Board;
import src.main.java.com.dtu.shared.model.FieldAction;
import src.main.java.com.dtu.shared.model.Player;
import src.main.java.com.dtu.shared.model.Space;

import java.io.Serializable;

public class Checkpoint extends FieldAction implements Serializable {
    private int checkpointNumber;

    public Checkpoint(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }

    @Override
    public boolean doAction(IGameController gameController, Space space) {
        Player player = space.getPlayer();
        Board board = gameController.getBoard();

        if (player != null && player.getCheckpointProgression() == this.checkpointNumber - 1 && board.getStep() == 4) {
            player.setCheckpointProgression(checkpointNumber);
        }

        if (player.getCheckpointProgression() == board.getWinCon() && board.getWinner() == null) {
            board.setWinner(player);
        }
        return false;
    }

    public int getCheckpointNumber() {
        return checkpointNumber;
    }

    public void setCheckpointNumber(int checkpointNumber) {
        this.checkpointNumber = checkpointNumber;
    }
}
