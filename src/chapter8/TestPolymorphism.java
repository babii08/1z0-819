package chapter8;

import org.junit.jupiter.api.Test;

public class TestPolymorphism extends Penguin{
    private int height = 8;
    public int getHeight() { return 8; }
    public static void main(String []fish) {
        new TestPolymorphism().printInfo();
    }

}

class Penguin {
    private int height = 3;
    public int getHeight() { return this.height ; }
    public void printInfo() {
        System.out.print(this.height);
    }
}
