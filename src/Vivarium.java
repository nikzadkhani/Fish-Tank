
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import java.util.*;

public class Vivarium implements Displayable, Animate {
	private Tank tank;
	public ArrayList<Component> vivarium = new ArrayList<Component>();

	final float SHARK_SCALE = 0.4f;
	final float FISH_SCALE = 0.1f;
	final float FOOD_SCALE = 0.05f;

	float sharkRadius = SHARK_SCALE;
	float fishRadius = FISH_SCALE;
	float foodRadius = FOOD_SCALE * 1.0f;

	public boolean foodAdded = false;

	private ArrayList<Point3D> walls = new ArrayList<Point3D>();

	GL2 mgl;

	public Vivarium() {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		vivarium.add(new SharkModel(new Point3D(0, 0, 0), SHARK_SCALE));
		vivarium.add(new SharkModel(new Point3D(-1, -1, -1), SHARK_SCALE));
//		vivarium.add(new FishModel(new Point3D(1, 0, 0), FISH_SCALE));
//		vivarium.add(new FishModel(new Point3D(1, 1, 0), FISH_SCALE));
		vivarium.add(new FishModel(new Point3D(1, -1.2, 1), FISH_SCALE));
		vivarium.add(new FishModel(new Point3D(0.3, -1, -1.2), FISH_SCALE));
		vivarium.add(new FishModel(new Point3D(-1.5, 0, 1), FISH_SCALE));
		vivarium.add(new FoodModel(new Point3D(-1, -1.5, 0), FOOD_SCALE));
//		vivarium.add(new FishModel(new Point3D(-1, -1, 1), FISH_SCALE));

		// vertices of the walls
//		walls.add(new Point3D(1.5, 1.5, 1.5));
//		walls.add(new Point3D(1.5, 1.5, -1.5));
//		walls.add(new Point3D(1.5, -1.5, 1.5));
//		walls.add(new Point3D(1.5, -1.5, -1.5));
//		walls.add(new Point3D(-1.5, 1.5, 1.5));
//		walls.add(new Point3D(-1.5, 1.5, -1.5));
//		walls.add(new Point3D(-1.5, -1.5, 1.5));
//		walls.add(new Point3D(-1.5, -1.5, -1.5));
		// center of wall faces
//		walls.add(new Point3D(0, 0, 2));
//		walls.add(new Point3D(0, 0, -2));
//		walls.add(new Point3D(0, 2, 0));
//		walls.add(new Point3D(0, -2, 0));
//		walls.add(new Point3D(2, 0, 0));
//		walls.add(new Point3D(2, 0, 0));
	}

	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for (Component object : vivarium) {
			object.initialize(gl);
		}
		this.mgl = gl;
	}

	public void update(GL2 gl) {
		tank.update(gl);
		for (Component object : vivarium) {
			object.update(gl);
		}
		this.mgl = gl;
	}

	public void draw(GL2 gl) {
		tank.draw(gl);
		for (Component object : vivarium) {
			if (object instanceof FishModel){
				if (!((FishModel) object).isHit())
//					System.out.println(object.position());
//					System.out.println(object.deltaX + " " + object.deltaY + " " + object.deltaZ);
					object.draw(gl);
			} else {
				if (object instanceof FoodModel) {
					if (!((FoodModel) object).isEaten())
						object.draw(gl);
				} else {
					object.draw(gl);
				}
			}
		}
		this.mgl = gl;
	}

	@Override
	public void animationUpdate(GL2 gl) {
		ArrayList<Point3D> fishPositions = getCurrentFishPositions();
		ArrayList<Point3D> sharkPositions = getCurrentSharkPositions();
		ArrayList<FoodModel> foodModels = getCurrentFood();
//		System.out.println(fishPositions.get(0));
		for (Component creature : vivarium) {
			if (creature instanceof Animate) {
				if (creature instanceof SharkModel) {
					Point3D sharkDeltas = getSharkDelta((SharkModel) creature, fishPositions);
//					System.out.println(sharkDeltas.toString());
					((SharkModel) creature).setDeltas(sharkDeltas);
				}
				if (creature instanceof FishModel) {
					((FishModel) creature).setDeltas(getFishDelta((FishModel) creature, sharkPositions, foodModels));
				}
				((Animate) creature).animationUpdate(gl);
			}
		}
		this.mgl = gl;
	}

	public ArrayList<Point3D> getCurrentFishPositions() {
		ArrayList<Point3D> fishPositions = new ArrayList<Point3D>();
		for (Component creature: vivarium) {
			if (creature instanceof FishModel) {
				if (!((FishModel) creature).isHit()) {
					fishPositions.add(creature.position());
				} else {
//					System.out.println("Hit");
				}
			}
		}
		return fishPositions;
	}

	public ArrayList<Point3D> getCurrentSharkPositions() {
		ArrayList<Point3D> sharkPositions = new ArrayList<Point3D>();
		for (Component creature: vivarium) {
			if (creature instanceof SharkModel) {
				sharkPositions.add(creature.position());
			}
		}
		return sharkPositions;
	}

	public ArrayList<FoodModel> getCurrentFood() {
		ArrayList<FoodModel> foodModels = new ArrayList<FoodModel>();
		for (Component creature: vivarium) {
			if (creature instanceof FoodModel) {
				if (!((FoodModel) creature).isEaten)
					foodModels.add((FoodModel) creature);
			}
		}
		return foodModels;
	}

	public Point3D getFishDelta(FishModel fish, ArrayList<Point3D> sharkPositions, ArrayList<FoodModel> foodModels) {
		Point3D fishCurrPosition = fish.position();
		ArrayList<Point3D> grads = new ArrayList<Point3D>();
		for (Point3D point: sharkPositions) {
			if (!isColliding(fishCurrPosition, fishRadius, point, sharkRadius )) {
				grads.add(getGradient(fishCurrPosition, point).negative());
			} else {
				fish.wasEaten();
			}
		}
		ArrayList<Point3D> wallGrads = new ArrayList<Point3D>();
		for (Point3D point: walls) {
			grads.add(getGradient(fishCurrPosition, point).negative());
		}
		Point3D wallGrad = sumPoints(wallGrads).negative();
//		grads.add(wallGrad);
		for (FoodModel food: foodModels) {
			if (!isColliding(fishCurrPosition, fishRadius, food.position(), foodRadius )) {
				grads.add(getGradient(fishCurrPosition, food.position()));
			} else {
				food.setEaten();
			}
		}
		return sumPoints(grads);
	}

	public Point3D getSharkDelta(SharkModel shark, ArrayList<Point3D> fishPositions) {
		Point3D sharkCurrPosition = shark.position();
		ArrayList<Point3D> grads = new ArrayList<Point3D>();
		for (Point3D point: fishPositions) {
			grads.add(getGradient(sharkCurrPosition, point));
		}
		for (Point3D point: walls) {
			grads.add(getGradient(sharkCurrPosition, point).negative());
		}
		return sumPoints(grads);
	}

	public Point3D getGradient(Point3D p0, Point3D p1){

		double gaussianScalar = Math.exp(-p0.normSquared(p1));

		double x = -2 * gaussianScalar * (p0.x() - p1.x());
		double y = -2 * gaussianScalar * (p0.y() - p1.y());
		double z = -2 * gaussianScalar * (p0.z() - p1.z());
		Point3D rawGradient = new Point3D(x, y, z);

		return rawGradient;
	}

	public Point3D sumPoints(ArrayList<Point3D> points) {
		double x = 0;
		double y = 0;
		double z = 0;
		for (Point3D point : points) {
			x += point.x();
			y += point.y();
			z += point.z();
		}
		Point3D gradSum = new Point3D(x, y, z);
		double magnitude = gradSum.norm();
		if (magnitude > 0)
			return new Point3D(x/magnitude, y/magnitude, z/magnitude);
		return new Point3D(x, y, z);
	}


	public boolean isColliding(Point3D p0, float r0, Point3D p1, float r1) {
		double distance = p0.distanceTo(p1);
//		System.out.println(distance);
		if (distance < (r0 + r1))
			return true;
		return false;
	}

	public void addFood() {
		Random random = new Random();
		float x = (random.nextFloat()*4) -2;
		float y = -1.8f;
		float z = (random.nextFloat()*4) -2;
		FoodModel newFood = new FoodModel(new Point3D(x, y, z), FOOD_SCALE);
//		newFood.initialize(this.mgl);
		vivarium.add(newFood);
	}


	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// assign configurations in config_list to all Components in here
	}
}
