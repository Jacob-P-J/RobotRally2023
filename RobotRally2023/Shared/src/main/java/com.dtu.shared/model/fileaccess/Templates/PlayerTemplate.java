package src.main.java.com.dtu.shared.model.fileaccess.Templates;

import src.main.java.com.dtu.shared.model.Heading;

// This class is used to store the player data from the json file
public class PlayerTemplate {
    public String name;
    public String color;
    public int x;
    public int y;
    public Heading heading = Heading.SOUTH;

    public PlayerTemplate(String name, String color, int x, int y, Heading heading) {
        this.name = name;
        this.color = color;
        this.x = x;
        this.y = y;
        this.heading = heading;
    }
}