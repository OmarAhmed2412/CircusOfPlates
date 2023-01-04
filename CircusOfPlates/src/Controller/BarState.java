package Controller;


import eg.edu.alexu.csd.oop.game.GameObject;

public class BarState extends StatePattern {

    public BarState(GameObject g) {
        super(g);
    }

    @Override
    public void move( int x, int y) {
        g.setX(x);
    }

}
