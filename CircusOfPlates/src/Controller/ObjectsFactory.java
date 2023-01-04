/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import eg.edu.alexu.csd.oop.game.GameObject;
import Model.BarObject;
import Model.ImageObject;
import Model.PlateObject;
import java.awt.Color;
import Controller.StrategyPattern;

/**
 *
 * @author mohdn
 */
public class ObjectsFactory {
    public static GameObject createShape(String objectName, int x, int y, StrategyPattern strategy) {

        if (objectName == null) {
            return null;
        }
        if (objectName.equals("plate")) {
            return new PlateObject(x, y, "/plate" + ((int) (Math.random() * 1000) % 3 + 1) + ".png",0);
        }
        else if (objectName.equals("shelf")){
            return new BarObject(x, y, 200, true, Color.WHITE);
        }
        else if (objectName.equals("backGround")){
            return new ImageObject(x, y, strategy.getBGPath());
        }
        else if(objectName.equals("clown")){
            return Singleton.getInstance(x, y, "/clownfinal2.png");
        }
        else if (objectName.equals("bomb")){
            return new PlateObject(x, y, "/bomb.png", 0);
        }
         else if (objectName.equals("star")){
            return new PlateObject(x, y, "/star.png", 0);
        }
        return null;
    }
    
}
