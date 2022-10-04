package chapter9;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

public class InheritanceTest {

    interface Walk { public List move(); }
    interface Run extends Walk { public ArrayList move(); }
    class Leopard {
        public Integer move() throws NoClassDefFoundError{ // X
            return null;
        }

        public void whatHappensNext() throws IOException {

            try {
                System.out.println("Dsads");
            } catch(NoClassDefFoundError e) {
                e.printStackTrace();
            }
        }
    }
}
