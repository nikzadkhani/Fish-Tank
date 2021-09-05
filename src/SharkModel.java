import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.Random;


public class SharkModel extends Component implements Animate {
    private double rotateSpeed = 0.5;
    private final double MOVEMENT_SPEED = 0.01;

    private Component sharkHead;
    private Component sharkTail;

    public SharkModel(Point3D p, float scale) {
        super(new Point3D(p));

        sharkTail = new Component(new Point3D(0, 0, 0), new SharkTailDisplayable());
        sharkHead = new Component(new Point3D(0,0,0), new SharkHeadDisplayable());
        sharkTail.setColor(new FloatColor(0.3f, 0.6f, 1f));
        sharkHead.setColor(new FloatColor(0.3f, 0.6f, 1f));

        this.addChild(sharkHead);
        this.addChild(sharkTail);

        sharkHead.setYNegativeExtent(-10);
        sharkHead.setYPositiveExtent(10);

        sharkTail.setYNegativeExtent(-10);
        sharkTail.setYPositiveExtent(10);

        this.setScale(scale);

    }

    @Override
    public void setModelStates(ArrayList<Configuration> config_list) {
        if (config_list.size() > 1) {
            this.setConfiguration(config_list.get(0));
        }
    }

    @Override
    public void animationUpdate(GL2 gl) {
        if (sharkHead.checkRotationReachedExtent(Axis.Y) || sharkTail.checkRotationReachedExtent(Axis.Y)) {
            rotateSpeed = -rotateSpeed;
        }
        sharkTail.rotate(Axis.Y, -rotateSpeed * 0.5);
        sharkTail.rotate(Axis.Z, -rotateSpeed * 0.5);
        sharkHead.rotate(Axis.Y, rotateSpeed * 0.7);

        avoidWalls();
        Point3D newPosition = new Point3D(this.position().x() + deltaX,
                this.position().y() + deltaY, this.position().z() + deltaZ);

        this.setPosition(newPosition);

        Point3D prod = this.position().crossProduct(newPosition);
        float w = (float) Math.sqrt(
                this.position().dotProduct(this.position()) +
                newPosition.dotProduct(newPosition) +
                this.position().dotProduct(newPosition)
        );

        Quaternion rotation = new Quaternion(prod, w);
        System.out.println(rotation);
        this.setRotationQuaternion(rotation);

    }

    @Override
    public void update(GL2 gl) {
        super.update(gl);

//        System.out.println(this.position().toString());
    }

    public void setDeltas(Point3D deltas) {
        Random random = new Random();
        float randomPerturbation = random.nextFloat()*3;
        this.deltaX = deltas.x() * MOVEMENT_SPEED * randomPerturbation;
        this.deltaY = deltas.y() * MOVEMENT_SPEED * randomPerturbation;
        this.deltaZ = deltas.z() * MOVEMENT_SPEED * randomPerturbation;
        faceTowardsNewDirection();
    }


}
