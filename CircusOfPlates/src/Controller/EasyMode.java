/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

/**
 *
 * @author mohdn
 */
public class EasyMode implements StrategyPattern{
    @Override
    public int getSpeed() {
        return 2;
    }

    @Override
    public int getTimeout() {
        return 2;
    }
    @Override
    public int getBombProb(){
        return 10;
    }

    @Override
    public String getBGPath() {
        String path = "/easy_circus.png";
        return path;
    }

    @Override
    public int getStarProb() {
        return 5;
    }

}
