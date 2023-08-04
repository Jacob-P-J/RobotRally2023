package com.dtu.shared.model.fieldTypes;

import com.dtu.shared.controller.GameController;
import com.dtu.shared.controller.IGameController;
import com.dtu.shared.model.FieldAction;
import com.dtu.shared.model.Heading;
import com.dtu.shared.model.Space;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


// implementation of the conveyor belt space
public class ConveyorBelt extends FieldAction implements Serializable {
    public Heading heading;
    public int power;

    // Getter
    public Heading getHeading() {
        return heading;
    }

    // Setter
    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public ConveyorBelt(Heading heading, int power) {
        this.heading = heading;
        this.power = power;
    }

    // Moves the player in the direction of the conveyor belt
    @Override
    public boolean doAction(@NotNull IGameController gameController, @NotNull Space space) {
        try {
            gameController.moveDirection(space.getPlayer(), power, this.heading);
        } catch (GameController.ImpossibleMoveException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
