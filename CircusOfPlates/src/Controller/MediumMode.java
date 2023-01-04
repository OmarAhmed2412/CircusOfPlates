/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author mohdn
 */
public class MediumMode implements StrategyPattern{
    @Override
    public int getSpeed() {
        return 4;
    }

    @Override
    public int getTimeout() {
        return 2;
    }

    @Override
    public int getBombProb() {
        return 7;
        
    }
    
    @Override
    public int getStarProb() {
        return 7;
    }
    
    @Override
    public String getBGPath() {
        String path = "/mid_circus.png";
        return path;
    }

}
