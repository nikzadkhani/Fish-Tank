import javax.media.opengl.GL2;
import java.util.ArrayList;

public class FoodModel extends Component implements Animate{
    private double rotationSpeed = 0.1;

    public boolean isEaten = false;

    Component food1;
    Component food2;
    Component food3;

    public FoodModel(Point3D p, float scale) {
        super(new Point3D(p));

        food1 = new Component(new Point3D(0, 0,0),
                new FoodSegmentDisplayable());
        food2 = new Component(
                new Point3D(0, FoodSegmentDisplayable.FOOD_RADIUS*1.5,0),
                new FoodSegmentDisplayable());
        food3 = new Component(
                new Point3D(0, FoodSegmentDisplayable.FOOD_RADIUS*3.0,0),
                new FoodSegmentDisplayable());

        food1.setColor(new FloatColor(0,1f,0.1f));
        food2.setColor(new FloatColor(0,1f,0.1f));
        food3.setColor(new FloatColor(0,1f,0.1f));


        this.addChild(food1);
        this.addChild(food2);
        this.addChild(food3);

        this.setXNegativeExtent(-5);
        this.setXPositiveExtent(5);
    }

    @Override
    public void setModelStates(ArrayList<Configuration> config_list) {
        if (config_list.size() > 1) {
            this.setConfiguration(config_list.get(0));
        }
    }

    @Override
    public void animationUpdate(GL2 gl) {
        if (this.checkRotationReachedExtent(Axis.X))
            rotationSpeed = -rotationSpeed;

        this.rotate(Axis.X, rotationSpeed);
        food3.rotate(Axis.X, -rotationSpeed);
        food2.rotate(Axis.X, rotationSpeed);
        food1.rotate(Axis.X, -rotationSpeed);

    }

    public void setEaten() {
        isEaten = true;
    }
    public boolean isEaten() {
        return isEaten;
    }
}
