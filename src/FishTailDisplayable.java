import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import java.lang.Math;

public class FishTailDisplayable implements Displayable{
    private
    int callListHandle;
    float scale;
    GLUquadric qd;

    public FishTailDisplayable() {
    }

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
        Double TAIL_LENGTH = 1.0;
        Double TAIL_FIN_WIDTH = BODY_RADIUS * 0.8;
        Double TAIL_FIN_LENGTH = BODY_RADIUS;
        Double HEAD_LENGTH = 0.8;
        Double DORSAL_FIN_LENGTH = 0.6;
        Double DORSAL_FIN_ANGLE = Math.toDegrees(Math.atan(TAIL_LENGTH/BODY_RADIUS));
        Double PECTORAL_FIN_LENGTH = 0.7;
        Double FIN_WIDTH = 0.2;
        Double FIN_LENGTH = 2 * FIN_WIDTH;
        gl.glPushMatrix();
        gl.glRotated(180, 0, 1, 0);
        // Tail
        gl.glPushMatrix();
        gl.glTranslated(0, 0, BODY_RADIUS * 0.5);
        gl.glScaled(0.8, 1, 1.5);
        glut.glutSolidCone(BODY_RADIUS, TAIL_LENGTH, 30, 30);

        // Tail Fin

        gl.glTranslated(0, 0, TAIL_LENGTH);
        gl.glRotated(180, 0, 1, 0);
        glut.glutSolidCone(TAIL_FIN_WIDTH, TAIL_FIN_LENGTH, 2, 1);
        gl.glPopMatrix();

        // Pelvic Fin
        gl.glPushMatrix();
        gl.glTranslated(0, -BODY_RADIUS * 0.6, TAIL_LENGTH * 0.7);
        gl.glRotated(70, 1,0,0);
        glut.glutSolidCone(FIN_WIDTH, FIN_LENGTH, 2, 1);
        gl.glPopMatrix();

        // Dorsal Fin
        gl.glPushMatrix();
        gl.glTranslated(0, BODY_RADIUS * 0.6, TAIL_LENGTH * 0.7);
        gl.glRotated(-70, 1,0,0);
        glut.glutSolidCone(FIN_WIDTH, FIN_LENGTH, 2, 1);
        gl.glPopMatrix();


        gl.glPopMatrix();

        gl.glEndList();
    }
}
