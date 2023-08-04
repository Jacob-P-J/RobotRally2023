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
package src.main.java.com.dtu.shared.model.fileaccess;

import src.main.java.com.dtu.shared.Config;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * A utility class reading strings from resources and arbitrary input streams.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class InputUtil {

    // Reads a string from an input stream, using guava's ByteSource
    public static String readString(final InputStream inputStream) {
        ByteSource byteSource = new ByteSource() {
            @Override
            public InputStream openStream() throws IOException {
                return inputStream;
            }
        };
        try {
            return byteSource.asCharSource(Charsets.UTF_8).read();
        } catch (IOException e) {
            return "";
        }
    }

    // Reads a string from a resource file
    public static String readResource(String relativeResourcePath) {
        ClassLoader classLoader = InputUtil.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(relativeResourcePath);
        return InputUtil.readString(inputStream);
    }

    // Getter for resource folders
    private static File[] getFolder(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = loader.getResource(folder);
        String path = url.getPath();
        return new File(path).listFiles();
    }

    // Getter for board names
    public static List<String> getBoardNames() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        List<String> boards = new ArrayList<>();
        try {
            boards = Files.list(Paths.get(loader.getResource(Config.BOARDSFOLDER).toURI()))
                    .toList().stream().map(file -> file.getFileName().toString()).toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return boards;
    }

    // Getter for saved games
    public static ArrayList<String> getSavedGames() {
        Path path = Path.of(System.getenv("APPDATA"), "Robotrally", Config.GAMESFOLDER);
        File[] savedGames = new File(String.valueOf(path)).listFiles();
        ArrayList<String> saves = new ArrayList<>();
        for (File savedGame : savedGames) {
            String name = savedGame.getName();
            saves.add(name);
        }
        return saves;
    }
}
