package chapter12;

public class Human implements Walk{
    @Override
    public String getSpeed() {
        return Walk.super.getSpeed();
    }
//    @Override
//    public Integer getSpeed() {
//        return Walk.super.getSpeed();
//    }
}
