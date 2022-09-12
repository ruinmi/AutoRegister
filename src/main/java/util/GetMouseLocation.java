package util;

import java.awt.*;

/**
 * created by elio on 12/09/2022
 */
public class GetMouseLocation {
    public static void main(String[] args) {
        int i = 0;
        while (i++ < 50) {
            Point p = MouseInfo.getPointerInfo().getLocation();
            System.out.println(p.getX() + "," + p.getY());
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
