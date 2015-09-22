package com.android.cy.androidmazegame.SceneManager;

import android.content.Context;
import android.util.Log;

import com.android.cy.androidmazegame.Objects.Cube;
import com.android.cy.androidmazegame.Objects.Wall;
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

    private class MazeMapUnit {
        Vector3D position;
        float angle;
        public MazeMapUnit(Vector3D pos, float a) {
            position = pos;
            angle = a;
        }
    }

    private Vector<MazeMapUnit> mazeMap;
    private Vector<Wall> mazeMapObjects;

    private SceneManager sceneManager;

    public MazeMap(SceneManager sm) {
        mazeMap = new Vector<MazeMapUnit>();
        mazeMapObjects = new Vector<Wall>();

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

                    if (nextLine.charAt(j) == '#') {
                        Log.v("MazeMap", j * MAZE_UNIT_WIDTH + " " + i * MAZE_UNIT_WIDTH);
                        MazeMapUnit tempUnit = new MazeMapUnit(
                                new Vector3D( j * MAZE_UNIT_WIDTH, 0, i * MAZE_UNIT_WIDTH),
                                0);
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
}
