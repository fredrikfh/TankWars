package com.game.tankwars.model;

import com.badlogic.gdx.math.BSpline;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class TerrainGenerator {
    float xStart;
    float xEnd;
    float yMax;
    float yMin;
    int points;
    Vector2[] controlPoints;
    ArrayList<Integer> seedArray;

    public TerrainGenerator(float xStart, float xEnd, float yMin, float yMax, int points, ArrayList<Integer> seedArray){
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yMax = yMax;
        this.yMin = yMin;
        this.points = points;
        this.seedArray = seedArray;

        controlPoints = new Vector2[points];

        controlPoints[0] = new Vector2(xStart, (yMax + yMin)/2);
        controlPoints[points - 1] = new Vector2(xEnd + 2, (yMax + yMin)/2);

        for (int i = 1; i < points - 1; i++){
            controlPoints[i] = new Vector2( xEnd*((float)i/((float) points - 2)), seedArray.get(i));
        }
    }

    public Vector2[] generateVertices(int vertNumber) {

        Vector2[] vertices = new Vector2[vertNumber];
        Vector2 outStart = new Vector2();
        Vector2 outEnd = new Vector2();
        Vector2 tmp = new Vector2();

        BSpline.cubic(outStart, 0, controlPoints, false, tmp);
        vertices[0] = outStart;

        BSpline.cubic(outEnd, 1, controlPoints, false, tmp);
        vertices[vertNumber-1] = outEnd;

        for (int i = 1; i < vertNumber - 1; i++){
            Vector2 out = new Vector2();
            float t = (float)i/((float) vertNumber - 1);
            BSpline.cubic(out, t, controlPoints, false, tmp);
            vertices[i] = out;
        }

        return vertices;
    }

}