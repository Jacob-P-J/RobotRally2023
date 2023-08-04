package src.main.java.com.dtu.shared.model.fileaccess;

import src.main.java.com.dtu.shared.model.FieldAction;
import src.main.java.com.dtu.shared.model.Heading;
import com.dtu.shared.model.fieldTypes.Checkpoint;
import com.dtu.shared.model.fieldTypes.ConveyorBelt;
import com.dtu.shared.model.fieldTypes.RotateGear;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class FieldActionTypeAdapter extends TypeAdapter<FieldAction> {
    @Override
    // Write the field action to the json file
    public void write(JsonWriter writer, FieldAction value) throws IOException {
        writer.beginObject();
        writer.name("type");
        if (value instanceof Checkpoint checkpoint) {
            writer.value("checkPoint");
            writer.name("checkPointNumber");
            writer.value(checkpoint.getCheckpointNumber());
        }
        if (value instanceof ConveyorBelt conveyorBelt) {
            writer.value("conveyorBelt");
            writer.name("heading");
            writer.value(conveyorBelt.getHeading().toString());
            writer.name("power");
            writer.value(conveyorBelt.power);
        }
        if (value instanceof RotateGear rotateGear) {
            writer.value("rotateGear");
            writer.name("direction");
            writer.value(rotateGear.direction);
        }
        writer.endObject();
    }

    // Read the field action from the json file
    @Override
    public FieldAction read(JsonReader reader) throws IOException {
        reader.beginObject();
        FieldAction action = null;
        if ("type".equals(reader.nextName())) {
            String type = reader.nextString();
            if ("checkPoint".equals(type)) {
                reader.nextName();
                int checkPointNumber = reader.nextInt();
                action = new Checkpoint(checkPointNumber);
            }
            if ("conveyorBelt".equals(type)) {
                reader.nextName();
                String heading = reader.nextString();
                reader.nextName();
                int power = reader.nextInt();
                action = new ConveyorBelt(Heading.valueOf(heading), power);
            }
            if ("rotateGear".equals(type)) {
                reader.nextName();
                int direction = reader.nextInt();
                action = new RotateGear(direction);
            }
        }
        reader.endObject();
        return action;
    }
}
