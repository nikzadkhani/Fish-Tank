import com.jogamp.opengl.util.gl2.GLUT;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

public class SharkHeadDisplayable implements Displayable{
    private
    int callListHandle;
    GLUquadric qd;

    public SharkHeadDisplayable() {
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
        Double TAIL_LENGTH = 2.0;
        Double TAIL_FIN_LENGTH = 0.5;
        Double HEAD_LENGTH = 0.8;
        Double DORSAL_FIN_LENGTH = 0.6;
        Double DORSAL_FIN_ANGLE = Math.toDegrees(Math.atan(TAIL_LENGTH/BODY_RADIUS));
        Double PECTORAL_FIN_LENGTH = 0.7;

        gl.glPushMatrix();
        // Head
        glut.glutSolidCone(BODY_RADIUS, HEAD_LENGTH, 7, 2);
        gl.glPushMatrix();
        gl.glScaled(1, 1, 0.7);
        glut.glutSolidSphere(BODY_RADIUS * 0.85, 20,20);
        gl.glPopMatrix();

        // Pectoral Fin
        gl.glPushMatrix();
        gl.glRotated(125, 0, 0, 1);
        gl.glTranslated(0, BODY_RADIUS * 0.7, 0);
        gl.glRotated(-DORSAL_FIN_ANGLE, 1, 0, 0);
        glut.glutSolidCone(BODY_RADIUS * 0.5, PECTORAL_FIN_LENGTH, 2, 1);
        gl.glPopMatrix();

        // Pectoral Fin
        gl.glPushMatrix();
        gl.glRotated(-125, 0, 0, 1);
        gl.glTranslated(0, BODY_RADIUS * 0.7, 0);
        gl.glRotated(-DORSAL_FIN_ANGLE, 1, 0, 0);
        glut.glutSolidCone(BODY_RADIUS * 0.5, PECTORAL_FIN_LENGTH, 2, 1);
        gl.glPopMatrix();


        gl.glPopMatrix();

        gl.glEndList();
    }


}
