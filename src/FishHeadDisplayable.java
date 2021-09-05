import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import java.lang.Math;

public class FishHeadDisplayable implements Displayable{
    private
    int callListHandle;
    float scale;
    GLUquadric qd;

    public FishHeadDisplayable() {}

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

        Double BODY_RADIUS = 0.4;

        gl.glPushMatrix();
        // Head
        gl.glPushMatrix();
        gl.glScaled(0.8,1,1.5);
        glut.glutSolidSphere(BODY_RADIUS, 30, 30);
        gl.glPopMatrix();



        gl.glPopMatrix();

        gl.glEndList();
    }


}
