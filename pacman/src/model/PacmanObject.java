package model;

import java.util.Random;

public class pacman {
    int positionX;
    int positionY;
    int size_of_map;

    //constructor to take size of map as parameter
    public pacman(int size_of_map) {
        this.positionX = (this.positionY = 0);
        this.size_of_map = size_of_map;
    }



    //this method is to move pacman randomly
    public void move() {

        int move_position_x = 0;
        int move_postion_y = 0;
        Random random = new Random();
        do {

            move_position_x = 0;
            move_postion_y = 0;
            int direction = random.nextInt(6);
            switch (direction) {
                case 0:
                    move_postion_y = -1;
                    break;
                case 1:
                case 4:
                    move_postion_y = 1;
                    break;
                case 2:
                case 5:
                    move_position_x = 1;
                    break;
                case 3:
                    move_position_x = -1;
            }
        } while ((this.positionX + move_position_x < 0) || (this.positionX + move_position_x >= size_of_map) || (
                this.positionY + move_postion_y < 0) || (this.positionY + move_postion_y >= size_of_map));
        this.positionX += move_position_x;
        this.positionY += move_postion_y;
    }

    //getters for variables of position x
    public int getPositionX() {
        return positionX;
    }
    //getters for variables of position y
    public int getPositionY() {
        return positionY;
    }
}

