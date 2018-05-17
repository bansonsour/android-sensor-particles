package simplepath;

import android.content.Context;

import util.ShaderHelper;
import util.TextResourceReader;

import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;

public class SimplePathShaderProgram {
    private static final String U_MODEL = "u_Model";
    private static final String U_VIEW = "u_View";
    private static final String U_PROJ = "u_Proj";
    private static final String U_COLOR = "u_Color";
    private static final String A_POSITION = "a_Position";

    private int program;
    private int uModelLocation;
    private int uViewLocation;
    private int uProjectionLocation;
    private int uColorLocation;
    private int aPositionLocation;

    public SimplePathShaderProgram(Context context, int vertexShaderId, int fragmentShaderId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader
                .readTextFileFromResource(context, vertexShaderId),
                TextResourceReader
                .readTextFileFromResource(context, fragmentShaderId));

        uModelLocation = glGetUniformLocation(program, U_MODEL);
        uViewLocation = glGetUniformLocation(program, U_VIEW);
        uProjectionLocation = glGetUniformLocation(program, U_PROJ);
        uColorLocation = glGetUniformLocation(program, U_COLOR);
        aPositionLocation = glGetAttribLocation(program, A_POSITION);
    }

    public void setMatrixUniforms(float[] modelMatrix, float[] viewMatrix, float[] projectionMatrix){
        glUniformMatrix4fv(uModelLocation, 1, false, modelMatrix, 0);
        glUniformMatrix4fv(uViewLocation, 1, false, viewMatrix, 0);
        glUniformMatrix4fv(uProjectionLocation, 1, false, projectionMatrix, 0);
    }

    public void setColorUniform(float r, float g, float b, float a) {
        glUniform4f(uColorLocation, r, g, b, a);
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

    public void useProgram() {
        glUseProgram(program);
    }
}
