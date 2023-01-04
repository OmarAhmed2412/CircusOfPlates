/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Singleton.java to edit this template
 */
package Controller;

import eg.edu.alexu.csd.oop.game.GameObject;
import Model.ClownObject;

/**
 *
 * @author mohdn
 */
public class Singleton {
    private static ClownObject clownObject = null;
    
    private Singleton() {
    }
    
    public static ClownObject getInstance(int x ,int y , String path) {
        if (clownObject == null)
            clownObject = new ClownObject(x, y, path);
  
        return clownObject;
    }
    
//    private static class SingletonHolder {
//
//        private static final Singleton INSTANCE = new Singleton();
//    }
}
