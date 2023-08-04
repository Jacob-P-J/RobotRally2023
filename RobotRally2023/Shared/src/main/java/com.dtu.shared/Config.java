package src.main.java.com.dtu.shared;

import java.util.Arrays;
import java.util.List;


public class Config {
    public final static List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    public final static String[] PLAYER_COLORS = {"blue", "cyan", "green", "magenta", "red", "yellow"};
    public final static int DEFAULT_BOARD_HEIGHT = 8;
    public final static int DEFAULT_BOARD_WIDTH = 8;
    public final static String GAMESFOLDER = "saved_games";
    public final static String BOARDSFOLDER = "boards";
}
