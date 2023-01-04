/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author mohdn
 */
public class HardMode implements StrategyPattern{
    @Override
    public int getSpeed() {
        return 5;
    }

    @Override
    public int getTimeout() {
        return 2;
    }

    @Override
    public int getBombProb() {
        return 5;
    }
    
    @Override
    public int getStarProb() {
        return 10;
    }
    
    @Override
    public String getBGPath() {
        String path = "/hard_circus.png";
        return path;
    }
}
