package src.main.java.com.dtu.shared.model.fieldTypes;

import src.main.java.com.dtu.shared.controller.IGameController;
import src.main.java.com.dtu.shared.model.FieldAction;
import src.main.java.com.dtu.shared.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

// Implementation of the Rotation gear space
public class RotateGear extends FieldAction implements Serializable {
    public int direction;

    public RotateGear(int direction) {
        super();
        this.direction = direction;
    }

    @Override
    public boolean doAction(@NotNull IGameController gameController, @NotNull Space space) {
        if (direction == 1){
            gameController.turnRight(space.getPlayer());
        }
        if (direction == 2){
            gameController.turnLeft(space.getPlayer());
        }
        return false;
    }
}