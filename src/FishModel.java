import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.Random;

public class FishModel extends Component implements Animate {
    private double rotateSpeed = 1.5;
    private final double MOVEMENT_SPEED = 0.005;

    private Component fishHead;
    private Component fishTail;



    private boolean isHit = false;

    public FishModel(Point3D p, float scale) {
        super(new Point3D(p));

        fishHead = new Component(new Point3D(0, 0, 0), new FishHeadDisplayable());
        fishTail = new Component(new Point3D(0, 0, 0), new FishTailDisplayable());

        fishHead.setColor(new FloatColor(1f, 0.6f, 0.2f));
        fishTail.setColor(new FloatColor(1f, 0.6f, 0.2f));

        this.addChild(fishHead);
        this.addChild(fishTail);

        fishTail.setYNegativeExtent(-15);
        fishTail.setYPositiveExtent(15);

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
        if (fishTail.checkRotationReachedExtent(Axis.Y)) {
            rotateSpeed = -rotateSpeed;
        }

        fishTail.rotate(Axis.Y, rotateSpeed);
        fishTail.rotate(Axis.Z, rotateSpeed * 0.5);

        // Doesn't let escape box
        avoidWalls();

        Point3D newPosition = new Point3D(this.position().x() + deltaX,
                this.position().y() + deltaY, this.position().z() + deltaZ);

//        System.out.println(newPosition.toString());
        this.setPosition(newPosition);
    }

    public void setDeltas(Point3D deltas) {
        Random random = new Random();
        float randomPerturbation = random.nextFloat()*1;
        this.deltaX = deltas.x() * MOVEMENT_SPEED * randomPerturbation;
        this.deltaY = deltas.y() * MOVEMENT_SPEED * randomPerturbation;
        this.deltaZ = deltas.z() * MOVEMENT_SPEED * randomPerturbation;
        faceTowardsNewDirection();
    }

    public void wasEaten() {
        System.out.println("eaten");
        this.isHit = true;
    }

    public boolean isHit() {
        return this.isHit;
    }
}
