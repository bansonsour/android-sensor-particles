package simplepath;

import android.util.Log;

import util.VertexArray;

import static android.opengl.GLES20.GL_LINE_STRIP;
import static android.opengl.GLES20.glDrawArrays;

public class SimplePath {
    private static final String TAG = "SimplePath";
    private static final int BYTES_PER_FLOAT = 4;
    private static final int TOTAL_COMPONENT_COUNT = 3;

    private final int maxPointCount;
    private final float[] points;
    private final VertexArray vertexArray;

    private int currentPointCount;
    private int nextPoint;

    public SimplePath(int maxPointCount){
        points = new float[maxPointCount * TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT];
        vertexArray = new VertexArray(points);
        this.maxPointCount = maxPointCount;
    }

    public void addPoint(float[] values) {

        final int pointOffset = nextPoint * TOTAL_COMPONENT_COUNT;
        int currentOffset = pointOffset;
        nextPoint++;

        if(currentPointCount < maxPointCount) {
            currentPointCount++;
        }

        if(nextPoint == maxPointCount) {
            nextPoint = 0;
        }

        points[currentOffset++] = values[0];
        points[currentOffset++] = values[1];
        points[currentOffset++] = values[2];

        vertexArray.updateBuffer(points, pointOffset, TOTAL_COMPONENT_COUNT);
    }

    public void bindData(SimplePathShaderProgram simplePathShaderProgram){
        vertexArray.setVertexAttribPointer(0, simplePathShaderProgram.getPositionAttributeLocation(),
                TOTAL_COMPONENT_COUNT, TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT);
    }

    public void draw() {
        glDrawArrays(GL_LINE_STRIP, 0, currentPointCount);
    }
}
