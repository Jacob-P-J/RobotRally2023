package com.dtu.shared.model.fileaccess.Templates;

import java.util.ArrayList;
import java.util.List;

// This class is used to store the board data from the json file
public class BoardTemplate {
    public int width;
    public int height;
    public List<SpaceTemplate> spaces = new ArrayList<>();
    public List<PlayerTemplate> players = new ArrayList<>();
}
