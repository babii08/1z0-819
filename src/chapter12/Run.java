package chapter12;

public interface Run {
    default Integer getSpeed() {
        return 3;
    }
}
