package chapter20;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class testPathMethods {

    @Test
    void testToStringMethodOnPath() {

        Path path = Path.of("../../resource/pathForNIO2.txt");

        System.out.println(path.toString());
    }

    @Test
    void testGetNameCountMethodOnPath() {

        Path directory = Path.of("../../resource");

        //checks the number of elements in the path, not including root if absolute path - in this case
        // first element - ..
        // second element - ..
        // third element - resource
        System.out.println(directory.getNameCount());

        Path file = Path.of("../../resource/pathForNIO2.txt");

        //in this case
        // first element - ..
        // second element - ..
        // third element - resource
        // fourth element - pathForNIO2.txt
        System.out.println(file.getNameCount());
    }

    @Test
    void testGetNameMethodOnPath() {

        Path directory = Path.of("../../resource");

        for (int i = 0; i < directory.getNameCount(); i++) {
            //gets the name of each part of path - similar to what is in the comment on previous method
            System.out.println(directory.getName(i));
        }
    }

    @Test
    void testSubPathMethodOnPath() {
        Path path = Path.of("../../resource");

        // subpath takes a part of path. first argument is inclusive and last is exclusive.
        // The example below should return first element only which is - ..
        System.out.println(path.subpath(0, 1));

        //second argument should be grater because it is exclusive
        assertThrows(IllegalArgumentException.class, () -> {
            path.subpath(0, 0);
        });

        // second argument is too big. this path has only 3 elements
        assertThrows(IllegalArgumentException.class, () -> {
            path.subpath(0, 4);
        });

    }

    @Test
    void testGetFileName() {
        Path file = Paths.get("../../resources/pathForNIO2.txt");

        //shows file name
        assertEquals("pathForNIO2.txt", file.getFileName().toString());

        Path directory = Paths.get("../../resources");

        //shows directory name
        assertEquals("resources", directory.getFileName().toString());
    }

    @Test
    void testGetParentMethodOnPath() {
        Path file = Paths.get("../../resources/pathForNIO2.txt");

        Path parent = file.getParent();

        assertEquals("..\\..\\resources", parent.toString());
    }

    @Test
    void testGetRootMethodOnPath() {
        Path file = Paths.get("../../resources/pathForNIO2.txt");

        Path root = file.getRoot();
        //the root is null when dealing with relative paths
        assertNull(root);

        Path otherPath = null;
        try {
            otherPath = Path.of(new URI("file:///C:/Users/Andrian/Project/bvb-crawler/ocp/resource"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        assertEquals("C:\\", otherPath.getRoot().toString());
    }

    @Test
    void testIsAndToAbsoluteMethodOnPath() {
        Path path = Path.of("dummy.txt");

        assertFalse(path.isAbsolute());

        //for whatever reason the method bellow is not providing the full path to the dummy.txt which is in current folder.
        //most probably all code no matter if being part of a file with separate path, sees current path as being the project folder
        //which in this case is ocp folder
        System.out.println(path.toAbsolutePath());
    }

    @Test
    void testResolveMethodOnPath() throws URISyntaxException {
        Path path = Path.of(new URI("file:///dummy.txt"));

        assertEquals("\\dummy.txt\\newFolder", path.resolve("newFolder").toString());

        Path newPath = Path.of("newFolder");

        assertEquals("\\dummy.txt\\newFolder", path.resolve(newPath).toString());
    }

    @Test
    void testRelativizeMethodOnPath() {
        Path dummyFile = Path.of("dummyChapter");
        Path pathForNIO2 = Path.of("../../resource/pathForNIO2.txt");

        Path pathFromDummyToNIO2 = dummyFile.relativize(pathForNIO2);

        assertEquals("..\\..\\..\\resource\\pathForNIO2.txt", pathFromDummyToNIO2.toString());
    }

    @Test
    void testNormalizeMethodOnPath() {
        Path dummyFile = Path.of("chapter20\\..\\chapter20\\.\\dummyChapter");

        //for whatever reason system is seeing ocp and not chapter20 as being the current folder.
        //It seems like it doesn't matter in what file the code is written, it will see the root directory as the current folder
        System.out.println(dummyFile.toAbsolutePath());

        assertEquals("chapter20\\dummyChapter", dummyFile.normalize().toString());
    }

    @Test
    void testToRealPathMethodOnPath() throws IOException {
        Path realPath = Paths.get("src", "chapter20" ,"dummy.txt");

        //Check if the path is real and throws IOException. Accepts an optional LinkOption if don't want to follow symbolic  links
        System.out.println(realPath.toRealPath(LinkOption.NOFOLLOW_LINKS));
        assertNotNull(realPath.toRealPath(LinkOption.NOFOLLOW_LINKS));

        Path dummyPath = Paths.get("some", "invalid", "path");

        assertThrows(IOException.class, dummyPath::toRealPath);

    }
}
