import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class FoodSegmentDisplayable implements Displayable {
    private
    int callListHandle;
    float scale;
    GLUquadric qd;

    public static Double FOOD_RADIUS = 0.2;
    public static Double FOOD_THICKNESS_SCALE = 0.3;

    public FoodSegmentDisplayable() {}

    @Override
    public void draw(GL2 gl) {
        gl.glCallList(this.callListHandle);
    }

    @Override
    public void initialize(GL2 gl) {
        this.callListHandle = gl.glGenLists(1);
        gl.glNewList(this.callListHandle, GL2.GL_COMPILE);

        GLU glu = new GLU();
        this.qd = glu.gluNewQuadric();
        GLUT glut = new GLUT();

        gl.glPushMatrix();

        gl.glPushMatrix();
        gl.glScaled(FOOD_THICKNESS_SCALE, 1, FOOD_THICKNESS_SCALE);
        glut.glutSolidSphere(FOOD_RADIUS, 30, 30);
        gl.glPopMatrix();


        gl.glPopMatrix();

        gl.glEndList();
    }
}
