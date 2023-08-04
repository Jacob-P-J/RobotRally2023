package com.dtu.shared.model.fileaccess.Templates;

import com.dtu.shared.model.Heading;
import com.dtu.shared.model.FieldAction;

import java.util.ArrayList;
import java.util.List;

// This class is used to store the space data from the json file
public class SpaceTemplate {
    public int x;
    public int y;
    public List<Heading> walls = new ArrayList<>();
    public List<FieldAction> actions = new ArrayList<>();
}
