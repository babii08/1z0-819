package chapter20;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestingCreatingPath {

    @Test
    void createPathWithPathInterface() {
        //Create path with single string to the file/directory
        Path path = Path.of("..\\..\\resource\\pathForNIO2.txt");

        //Creates path given only the components. Path will insert system based separator itself
        Path otherPath = Path.of("..", "..", "resource", "pathForNIO2.txt");

        //It is not required for the path to exists when creating it.
        Path dummyPath = Path.of("dummy", "path");
    }

    @Test
    void createPathWithPathsClass() {
        //Be aware that Paths is a class and is different that Path - which is a interface
        //however it contains the same overloaded methods

        Path path = Paths.get("..\\..\\resource\\pathForNIO2.txt");
        Path otherPath = Paths.get("dummy", "path");
    }

    @Test
    void createPathFromURI() throws URISyntaxException {
        //Creates path using Paths class and URI to file. Some sistems require URI to be absolute (meaning to include schema)
        Path path = Paths.get(URI.create("file:///dummy.txt"));

        //Besides .create you can also create URI using its constructor. Be aware that this is trowing checked URISyntaxException
        Path otherPath = Path.of(new URI("file:///C:/Users/Andrian/Project/bvb-crawler/ocp/resource"));

        //there is method to create URI from path
        URI uri = otherPath.toUri();
        System.out.println(uri);

        //and path from URI
        System.out.println(uri.getPath());
    }

    @Test
    void createFileSystemClass() throws URISyntaxException {
        //Creating FileSystem using FileSystem class getDefault() method
        FileSystem fs = FileSystems.getDefault();

        //Getting path from FileSystem instance
        Path path = FileSystems.getDefault().getPath("dummy.txt");

        //The power of FileSystem is that it provides possibility to connect to a remote file system

//        FileSystem fileSystem = FileSystems.getFileSystem(new URI("http://www.selikoff.net"));
//        Path remotePath = fileSystem.getPath("duck.txt");

        //the above is commented as it doesn't work. More details https://coderanch.com/t/682162/certification/URI-http-Provider-http
    }

    @Test
    void createPathFromFile() {
        Path path = FileSystems.getDefault().getPath("dummy.txt");

        //converting to file
        File file = path.toFile();
        System.out.println(file);

        //converting back to path
        System.out.println(file.toPath());
    }
}
