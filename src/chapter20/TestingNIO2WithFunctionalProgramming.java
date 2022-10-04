package chapter20;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class TestingNIO2WithFunctionalProgramming {

    private void copyFolder(Path source, Path destination) {
        try {
            Files.copy(source, destination);
            if (Files.isDirectory(source)) {
                try(Stream<Path> paths = Files.list(source)) {
                    paths.forEach(path -> copyFolder(path, destination.resolve(path.getFileName())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testListingDirectoryContents() {
        Path path = Paths.get("src\\chapter20\\dummyChapter");

        try(Stream<Path> files = Files.list(path)) {
            files.forEach(System.out::println);
        } catch (IOException exception) {
            exception.printStackTrace();
        }


        //performing a deep copy
        copyFolder(path, Path.of("src\\chapter20\\copiedDummyChapter"));
    }

    @Test
    void testingWalkingDirectory() {
        Path path = Paths.get("src\\chapter20");

        try(Stream<Path> paths = Files.walk(path, 4)) {
            paths.forEach(p -> {
                try {
                    System.out.println(Files.size(p));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void searchingDirectoryWithFind() {
        Path path = Paths.get("src\\chapter20");

        try(Stream<Path> paths = Files.find(path, 5, (p, bfa) -> bfa.size() > 500)) {
            paths.forEach(pa -> {
                try {
                    System.out.println("File :" + pa + " is of size " + Files.size(pa));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Test
    void readingFileWithLines() {
        Path path = Paths.get("src");

        try(Stream<String> paths = Files.lines(path)) {
            paths.forEach(System.out::println);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
