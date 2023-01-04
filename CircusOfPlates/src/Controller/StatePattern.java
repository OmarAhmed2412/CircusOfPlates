package Controller;


import eg.edu.alexu.csd.oop.game.GameObject;

public abstract class StatePattern {

    protected GameObject g;

    public StatePattern(GameObject g) {
        this.g = g;
    }

    public abstract void move(int x, int y);
}
