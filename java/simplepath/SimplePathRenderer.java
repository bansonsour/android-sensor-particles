package simplepath;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.janakiev.sensorparticles.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.perspectiveM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;

public class SimplePathRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "SimplePathRenderer";
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];

    private final Context context;
    private SimplePath simplePathAcc;
    private SimplePath simplePathGyro;
    private SimplePath simplePathMagnet;
    private SimplePathShaderProgram simplePathShaderProgram;

    private long globalStartTime;

    public SimplePathRenderer(Context context){
        this.context = context;
    }

    public void addAccSensorData(float[] values) {
        values[0] /= 20;
        values[1] /= 20;
        values[2] /= 20;

        if(simplePathAcc == null) simplePathAcc = new SimplePath(2000);
        simplePathAcc.addPoint(values);
    }

    public void addGyroSensorData(float[] values) {
        values[0] /= 20;
        values[1] /= 20;
        values[2] /= 20;

        if(simplePathGyro == null) simplePathGyro = new SimplePath(2000);
        simplePathGyro.addPoint(values);
    }

    public void addMagnetSensorData(float[] values) {
        values[0] /= 20;
        values[1] /= 20;
        values[2] /= 20;

        if(simplePathMagnet == null) simplePathMagnet = new SimplePath(2000);
        simplePathMagnet.addPoint(values);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        simplePathShaderProgram = new SimplePathShaderProgram(context,
                R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);

        simplePathAcc = new SimplePath(2000);
        simplePathGyro = new SimplePath(2000);
        simplePathMagnet = new SimplePath(2000);

        globalStartTime = System.nanoTime();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0,0,width, height);

        perspectiveM(projectionMatrix, 0, 45, (float) width / (float) height, 1f, 10f);
        setLookAtM(viewMatrix, 0,
                3.0f, 3.0f, 3.0f,
                0.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f);
        setIdentityM(modelMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;
        setIdentityM(modelMatrix, 0);
        rotateM(modelMatrix, 0, currentTime * 20.0f, 0.0f, 0.0f, 1.0f);

        simplePathShaderProgram.useProgram();
        simplePathShaderProgram.setMatrixUniforms(modelMatrix, viewMatrix, projectionMatrix);

        simplePathShaderProgram.setColorUniform(1.0f, 0.0f, 0.0f, 1.0f);
        simplePathAcc.bindData(simplePathShaderProgram);
        simplePathAcc.draw();

        simplePathShaderProgram.setColorUniform(0.0f, 1.0f, 0.0f, 1.0f);
        simplePathGyro.bindData(simplePathShaderProgram);
        simplePathGyro.draw();

        simplePathShaderProgram.setColorUniform(0.0f, 0.0f, 1.0f, 1.0f);
        simplePathMagnet.bindData(simplePathShaderProgram);
        simplePathMagnet.draw();
    }
}
