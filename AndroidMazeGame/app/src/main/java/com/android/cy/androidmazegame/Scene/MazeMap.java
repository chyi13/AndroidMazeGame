package com.android.cy.androidmazegame.Scene;

import android.content.Context;

import com.android.cy.androidmazegame.Objects.Cube;
import com.android.cy.androidmazegame.Utils.Vector3D;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Created by Administrator on 2015/9/22.
 */
public class MazeMap {

    public final static float MAZE_UNIT_WIDTH = 10.f;
    public final static float MAZE_UNIT_HEIGHT = 10.0f;

    private final static int MAZE_WIDTH = 11;
    private final static int MAZE_HEIGHT = 11;

    private static char[][] rawMap = new char[MAZE_WIDTH][MAZE_HEIGHT];

    private final static float mapOffsetX = 5.5f * MAZE_UNIT_WIDTH;
    private final static float mapOffsetY = 5.5f * MAZE_UNIT_WIDTH;

    private class MazeMapUnit {
        Vector3D position;
        public MazeMapUnit(Vector3D pos) {
            position = pos;
        }
    }

    private Vector<MazeMapUnit> mazeMap;

    private SceneManager sceneManager;

    public MazeMap(SceneManager sm) {
        mazeMap = new Vector<MazeMapUnit>();
        sceneManager = sm;
    }

    public boolean readMazeMap(Context context, int resourceId) {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

        String nextLine;
        int i = 0, j = 0;  // row, column
        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                for (j = 0; j< nextLine.length(); j++) {
                    char tempChar = nextLine.charAt(j);
                    rawMap[i][j] = tempChar;
                    if (tempChar == '#') {
                        MazeMapUnit tempUnit = new MazeMapUnit(new Vector3D( (j- 5.5f) * MAZE_UNIT_WIDTH, 0.0f, (i - 5.5f) * MAZE_UNIT_WIDTH));
                        mazeMap.add(tempUnit);
                    }
                }
                i++;
            }
        }
        catch (IOException e)
        {
            return false;
        }

        for (MazeMapUnit unit: mazeMap) {
        //    Wall tempWall = new Wall(sceneManager.getContext(), MAZE_UNIT_WIDTH, MAZE_UNIT_HEIGHT);
            Cube tempCube = new Cube(sceneManager.getContext());
            tempCube.setPosition(unit.position);
            sceneManager.addObject(tempCube);
        }

        return true;
    }

    public Vector<MazeMapUnit> getMazeMap() {
        return mazeMap;
    }

    public static boolean checkForCollision(float x, float y) {
        int tUnitX = (int) ((x + 60)/ MAZE_UNIT_WIDTH);
        int tUnitY = (int) ((y + 60)/ MAZE_UNIT_WIDTH);
        return (rawMap[tUnitY][tUnitX] == '#');
    }
}
