package Controller;

import java.util.LinkedList;
import java.util.List;

import eg.edu.alexu.csd.oop.game.GameObject;
import eg.edu.alexu.csd.oop.game.World;
import Model.BarObject;
import Model.ClownObject;
import Model.ImageObject;
import Model.PlateObject;
import java.awt.Color;
import javax.naming.spi.ObjectFactory;

/**
 *
 * @author moham
 */
public class Circus implements World {

    private static int MAX_TIME = 1 * 60 * 1000;	// 1 minute
    private int score = 0;
    private long startTime = System.currentTimeMillis();
    private int LIVES = 3;
    private final int width;
    private final int height;
    private StrategyPattern strategy;
    private final List<GameObject> constant = new LinkedList<GameObject>();
    private final List<GameObject> moving = new LinkedList<GameObject>();
    private final List<GameObject> control = new LinkedList<GameObject>();
    private List<GameObject> leftHand = new LinkedList<GameObject>();
    private List<GameObject> rightHand = new LinkedList<GameObject>();

    public Circus(int screenWidth, int screenHeight, StrategyPattern strategypattern) {
        width = screenWidth;
        height = screenHeight;
        strategy = strategypattern;
        // constant objects (backgroud, bars)
        GameObject backGround = ObjectsFactory.createShape("backGround", width / 1000, height / 1000, strategy);
        //ImageObject backGround = new ImageObject(width / 1000, height / 1000, "/circus3.png");
        constant.add(backGround);
        GameObject leftShelf = ObjectsFactory.createShape("shelf", 0, height - 500, strategy);
        //BarObject leftShelf = new BarObject(0, height - 500, 200, true, Color.WHITE);
        constant.add(leftShelf);
        GameObject rightShelf = ObjectsFactory.createShape("shelf", 600, height - 500, strategy);
        //BarObject rightShelf = new BarObject(600, height - 500, 200, true, Color.WHITE);
        constant.add(rightShelf);
        // control objects (clown)
        GameObject clown = ObjectsFactory.createShape("clown", screenWidth / 3, (int) (screenHeight * 0.55), strategy);
        //ClownObject clown = new ClownObject(screenWidth / 3, (int) (screenHeight * 0.55), "/clownfinal2.png");
        control.add(clown);
        // moving objects (plates)
        spawnPlates(10);
    }

    private boolean intersect(GameObject o1, GameObject o2) {
        return (Math.abs((o1.getX() + o1.getWidth() / 2) - (o2.getX() + o2.getWidth() / 2)) <= o1.getWidth()) && (Math.abs((o1.getY() + o1.getHeight() / 2) - (o2.getY() + o2.getHeight() / 2)) <= o1.getHeight());
    }
    int caughtPlatesCounter = 0;

    @Override
    public boolean refresh() {
        boolean timeout = System.currentTimeMillis() - startTime > MAX_TIME; // time end and game over
        GameObject clown = control.get(0);
        GameObjectIterator movingIterator = new GameObjectIterator(moving);

        while (movingIterator.hasNext()) {
            GameObject o = movingIterator.next();

            int type = ((PlateObject) o).getType();
            if (o.getX() + 200 < width / 2 && type == 1) {
                ((PlateObject) o).setCurrentState(new BarState(o));
                ((PlateObject) o).getCurrentState().move((o.getX() + strategy.getSpeed()), 0);
                // o.setX((int) (o.getX() + getSpeed()));
            } else if (o.getX() - 130 > width / 2 && type == 2) {
                ((PlateObject) o).setCurrentState(new BarState(o));
                ((PlateObject) o).getCurrentState().move(o.getX() - strategy.getSpeed(), 0);
                //o.setX((int) (o.getX() - getSpeed()));
            } else {
                if (type == 1) {
                    ((PlateObject) o).setCurrentState(new MovingState(o));
                    ((PlateObject) o).getCurrentState().move((int) (o.getX() + (o.getX() * 1000) % 3), (int) (o.getY() + Math.random() * 1000 % 3));
//                    o.setX((int) (o.getX() + (o.getX() * 1000) % 3));
//                    o.setY((int) (o.getY() + Math.random() * 1000 % 3));
                } else if (type == 2) {
                    ((PlateObject) o).setCurrentState(new MovingState(o));
                    ((PlateObject) o).getCurrentState().move((int) (o.getX() - Math.random() * 1000 % 3), (int) (o.getY() + Math.random() * 1000 % 3));
//                    o.setX((int) (o.getX() - Math.random() * 1000 % 3));
//                    o.setY((int) (o.getY() + Math.random() * 1000 % 3));
                }
            }
            if (leftHand.isEmpty()) {
                if (clownCatchLeftHanded(o)) {
                    caughtPlatesCounter++;
                    if (caughtPlatesCounter >= 2) {
                        spawnPlates(1);
                        caughtPlatesCounter = 0;
                    }
                    moving.remove(o);
                    if (((PlateObject) o).getPath().equals("/bomb.png")) {
                        score = Math.max(0, score - 1);
                        //spawnBombs(1);
                        LIVES--;
                        AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\bomb_sound_effect.wav");
                        audio.play();
                        if (LIVES <= 0) {
                            return false;
                        }

                    } else if (((PlateObject) o).getPath().equals("/star.png")) {
                        score++;
                        AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\star_sound_effect.wav");
                        audio.play();

                    } else {
                        ((PlateObject) o).setC((ClownObject) clown);
                        ((PlateObject) o).setX(clown.getX());         //Magnet
                        ((PlateObject) o).setY(clown.getY() - ((PlateObject) o).getHeight());
                        ((PlateObject) o).setHorizontalOnly(true);
                        ((PlateObject) o).setType(1);
                        control.add(o);
                        leftHand.add(o);
                    }
                }
            } else if (intersect(o, leftHand.get(leftHand.size() - 1))) {
                //o.setY(o.getY());
                caughtPlatesCounter++;
                if (caughtPlatesCounter >= 2) {
                    spawnPlates(1);
                    caughtPlatesCounter = 0;
                }
                moving.remove(o);
                if (((PlateObject) o).getPath().equals("/bomb.png")) {
                    score = Math.max(0, score - 1);
                    //spawnBombs(1);
                    LIVES--;
                    AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\bomb_sound_effect.wav");
                    audio.play();
                    if (LIVES <= 0) {
                        return false;
                    }
                } else if (((PlateObject) o).getPath().equals("/star.png")) {
                    score++;
                    AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\star_sound_effect.wav");
                    audio.play();

                } else {
                    ((PlateObject) o).setC((ClownObject) clown);
                    ((PlateObject) o).setX(clown.getX());          //Magnet
                    ((PlateObject) o).setY(leftHand.get(leftHand.size() - 1).getY() - ((PlateObject) o).getHeight());
                    ((PlateObject) o).setHorizontalOnly(true);
                    ((PlateObject) o).setType(1);
                    control.add(o);
                    leftHand.add(o);
                }
            }
            if (rightHand.isEmpty()) {
                if (clownCatchRightHanded(o)) {
                    caughtPlatesCounter++;
                    if (caughtPlatesCounter >= 2) {
                        spawnPlates(1);
                        caughtPlatesCounter = 0;
                    }
                    moving.remove(o);
                    if (((PlateObject) o).getPath().equals("/bomb.png")) {
                        score = Math.max(0, score - 1);
                        //spawnBombs(1);
                        LIVES--;
                        AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\bomb_sound_effect.wav");
                        audio.play();
                        if (LIVES <= 0) {
                            return false;
                        }
                    } else if (((PlateObject) o).getPath().equals("/star.png")) {
                        score++;
                        AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\star_sound_effect.wav");
                        audio.play();

                    } else {
                        ((PlateObject) o).setC((ClownObject) clown);
                        ((PlateObject) o).setX(clown.getX() + clown.getWidth() - ((PlateObject) o).getWidth());        //Magnet
                        ((PlateObject) o).setY(clown.getY() - ((PlateObject) o).getHeight());
                        ((PlateObject) o).setHorizontalOnly(true);
                        ((PlateObject) o).setType(2);
                        control.add(o);
                        rightHand.add(o);
                    }
                }
            } else if (intersect(o, rightHand.get(rightHand.size() - 1))) {
                //o.setY(o.getY());
                caughtPlatesCounter++;
                if (caughtPlatesCounter >= 2) {
                    spawnPlates(1);
                    caughtPlatesCounter = 0;
                }
                moving.remove(o);
                if (((PlateObject) o).getPath().equals("/bomb.png")) {
                    score = Math.max(0, score - 1);
                    //spawnBombs(1);
                    LIVES--;
                    AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\bomb_sound_effect.wav");
                    audio.play();
                    if (LIVES <= 0) {
                        return false;
                    }
                } else if (((PlateObject) o).getPath().equals("/star.png")) {
                    score++;
                    AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\star_sound_effect.wav");
                    audio.play();

                } else {
                    ((PlateObject) o).setC((ClownObject) clown);
                    ((PlateObject) o).setX(clown.getX() + clown.getWidth() - ((PlateObject) o).getWidth());            //Magnet
                    ((PlateObject) o).setY(rightHand.get(rightHand.size() - 1).getY() - ((PlateObject) o).getHeight());
                    ((PlateObject) o).setHorizontalOnly(true);
                    ((PlateObject) o).setType(2);
                    control.add(o);
                    rightHand.add(o);
                }
            }

            //adjust the plates to the clown
            adjustPlatesToClown();

            if (o.getY() >= getHeight()) {
                // reuse the plate in another position
                ((PlateObject) o).reusePlate();

            }
            updateHands();
            if (leftHand.size() >= 10 || rightHand.size() >= 10) {
                return false;
            }
        }

        return !timeout;
    }

    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getControlSpeed() {
        return 25;
    }

    @Override
    public List<GameObject> getConstantObjects() {
        return constant;
    }

    @Override
    public List<GameObject> getMovableObjects() {
        return moving;
    }

    @Override
    public List<GameObject> getControlableObjects() {
        return control;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public String getStatus() {
        return "Score=" + score + " | Time=" + Math.max(0, (MAX_TIME - (System.currentTimeMillis() - startTime)) / 1000) + " | " + loselife();    // update status
    }

    public void spawnPlates(int n) {
        for (int i = 0; i < n; i++) {
            int bombProb = (int) ((Math.random() * 1000) % strategy.getBombProb());
            int starProb = (int) ((Math.random() * 1000) % strategy.getStarProb());
            if (bombProb == 2) {
                GameObject bomb = ObjectsFactory.createShape("bomb", 0 - 100 * (3 * i), 55, strategy);
                ((PlateObject) bomb).setType(1);
                moving.add(bomb);

            } else if (starProb == 2) {
                GameObject star = ObjectsFactory.createShape("star", 0 - 100 * (3 * i), 70, strategy);
                ((PlateObject) star).setType(1);
                moving.add(star);
            } else {
                GameObject leftPlate = ObjectsFactory.createShape("plate", 0 - 100 * (3 * i), 92, strategy);
                ((PlateObject) leftPlate).setType(1);
                moving.add(leftPlate);
            }

        }
        for (int i = 0; i < n; i++) {
            int bombProb = (int) ((Math.random() * 1000) % strategy.getBombProb());
            int starProb = (int) ((Math.random() * 1000) % strategy.getStarProb());
            if (bombProb == 2) {
                GameObject bomb = ObjectsFactory.createShape("bomb", 800 + 100 * (3 * i), 55, strategy);
                ((PlateObject) bomb).setType(2);
                moving.add(bomb);
            } else if (starProb == 2) {
                GameObject star = ObjectsFactory.createShape("star", 800 + 100 * (3 * i), 70, strategy);
                ((PlateObject) star).setType(1);
                moving.add(star);
            } else {
                GameObject rightPlate = ObjectsFactory.createShape("plate", 800 + 100 * (3 * i), 92, strategy);
                ((PlateObject) rightPlate).setType(2);
                moving.add(rightPlate);
            }

        }
    }

//    public void spawnBombs(int n) {
//        int randomNumber = (int) (Math.random() * 1000) % 2;
//        if (randomNumber == 0) {
//            for (int i = 0; i < n; i++) {
//                GameObject bomb = ObjectsFactory.createShape("bomb", 0 - 100 * (3 * i), 55, strategy);
//                ((PlateObject) bomb).setType(1);
//                moving.add(bomb);
//            }
//        } else {
//            for (int i = 0; i < n; i++) {
//                GameObject bomb = ObjectsFactory.createShape("bomb", 800 + 100 * (3 * i), 55, strategy);
//                ((PlateObject) bomb).setType(2);
//                moving.add(bomb);
//            }
//        }
//    }
    private boolean clownCatchLeftHanded(GameObject o) {
        ClownObject clown = (ClownObject) control.get(0);
        return (Math.abs(clown.getX() - o.getX()) <= o.getWidth()
                && o.getY() == control.get(0).getY() - 10);
    }

    private boolean clownCatchRightHanded(GameObject o) {
        ClownObject clown = (ClownObject) control.get(0);
        return (Math.abs(clown.getX() + clown.getWidth() - o.getWidth() - o.getX()) <= o.getWidth()
                && o.getY() == control.get(0).getY() - 10);
    }

    private void updateHands() {
        if (leftHand.size() >= 3) {
            PlateObject p1 = (PlateObject) leftHand.get(leftHand.size() - 1);
            PlateObject p2 = (PlateObject) leftHand.get(leftHand.size() - 2);
            PlateObject p3 = (PlateObject) leftHand.get(leftHand.size() - 3);
            if (p1.getPath().equals(p2.getPath()) && p2.getPath().equals(p3.getPath())) {

                leftHand.remove(leftHand.size() - 1);
                leftHand.remove(leftHand.size() - 1);
                leftHand.remove(leftHand.size() - 1);
                control.remove(p1);
                control.remove(p2);
                control.remove(p3);
//                spawnPlates(1);
                score++;
                AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\coin_sound_effect.wav");
                audio.play();
            }
        }
        if (rightHand.size() >= 3) {
            PlateObject p1 = (PlateObject) rightHand.get(rightHand.size() - 1);
            PlateObject p2 = (PlateObject) rightHand.get(rightHand.size() - 2);
            PlateObject p3 = (PlateObject) rightHand.get(rightHand.size() - 3);
            if (p1.getPath().equals(p2.getPath()) && p2.getPath().equals(p3.getPath())) {
                rightHand.remove(rightHand.size() - 1);
                rightHand.remove(rightHand.size() - 1);
                rightHand.remove(rightHand.size() - 1);
                control.remove(p1);
                control.remove(p2);
                control.remove(p3);
//                spawnPlates(1);
                score++;
                AudioPlayer audio = AudioPlayer.getInstance("C:\\Users\\Blu-Ray\\NetBeansProjects\\myGame2_3\\res\\coin_sound_effect.wav");
                audio.play();
            }
        }
    }

    public void adjustPlatesToClown() {
        ClownObject clown = (ClownObject) control.get(0);
        GameObjectIterator leftIterator = new GameObjectIterator(leftHand);
        GameObjectIterator rightIterator = new GameObjectIterator(rightHand);
        while (leftIterator.hasNext()) {
            GameObject o1 = leftIterator.next();
            o1.setX(clown.getX());
        }
        while (rightIterator.hasNext()) {
            GameObject o2 = rightIterator.next();
            o2.setX(clown.getX() + clown.getWidth() - o2.getWidth());
        }
//        for (int i = 0; i < leftHand.size(); i++) {
//            leftHand.get(i).setX(clown.getX());
//        }
//        for (int i = 0; i < rightHand.size(); i++) {
//            rightHand.get(i).setX(clown.getX() + clown.getWidth() - rightHand.get(i).getWidth());
//
//        }
    }

    public String loselife() {
        if (LIVES == 3) {
            return "\u2764" + "\u2764" + "\u2764";
        } else if (LIVES == 2) {
            return "\u2764" + "\u2764";
        } else if (LIVES == 1) {
            return "\u2764";
        } else {
            return "";
        }
    }
}
