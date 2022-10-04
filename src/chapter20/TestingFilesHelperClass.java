package chapter20;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TestingFilesHelperClass {

    @Test
    void testExistsMethodOnFiles() {
        Path path = Paths.get("src", "chapter20", "dummy.txt");

        assertTrue(Files.exists(path, LinkOption.NOFOLLOW_LINKS));

        Path directoryPath = Paths.get("src", "chapter20", "dummyChapter");

        assertTrue(Files.exists(directoryPath, LinkOption.NOFOLLOW_LINKS));
    }

    @Test
    void testIsSameFileMethodOnFiles() {
        Path path = Paths.get("resource\\pathForNIO2.txt");

        Path otherPath = Path.of("src\\chapter20\\dummy.txt");

        try {
            assertFalse(Files.isSameFile(path, otherPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Path nonExistingPath = Paths.get("non\\existing\\path");
        Path samePath = nonExistingPath;

        //If 2 paths are equal, isSameFile will return true no matter the fact that files/directory don't exist
        try {
            assertTrue(Files.isSameFile(nonExistingPath, samePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If 1 file exists and the other don't - throw exception
        assertThrows(IOException.class, () -> Files.isSameFile(path, nonExistingPath));
    }

    @Test
    void testCreateDirectoryAndDirectoriesMethodsOnFiles() {
        UUID id = UUID.randomUUID();
        Path invalidPath = Paths.get("invalid\\path");
        Path validPath = Paths.get("src\\chapter20\\dummyChapter");

        //if path already exists or path is incorrect - throws exception
        assertThrows(IOException.class, () -> Files.createDirectory(invalidPath));
        assertThrows(IOException.class, () -> Files.createDirectory(validPath));

        Path validNewPath = validPath.resolve("newFolder" + id);
        try {
            Files.createDirectory(validNewPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.createDirectories(validPath.resolve("newDirectory" + id + "\\newFolder"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCopyMethodOnFiles() {
        Path source = Path.of("src\\chapter20\\dummy.txt");
        Path destination = Path.of("src\\chapter20\\dummyChapter\\copiedDummy.txt");

        //There are 3 types of copy to specify
        final var atomicMove = StandardCopyOption.ATOMIC_MOVE;
        final var copyAttributes = StandardCopyOption.COPY_ATTRIBUTES;
        final var replaceExisting = StandardCopyOption.REPLACE_EXISTING;

        try {
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCopyWithStreamsOnFiles() {
        Path pathToDummy = Paths.get("src\\chapter20\\dummy.txt");
        Path pathToDummyCopy = Paths.get("src\\chapter20\\dummyChapter\\dummy.txt");

        try(BufferedInputStream bis = new BufferedInputStream(new FileInputStream(pathToDummy.toFile()))) {
            Files.copy(bis, pathToDummyCopy, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pathToDummyCopy.toFile()))) {
            Files.copy(pathToDummy, bos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testMoveAndRenameMethodOnFiles() {
        Path pathToDummy = Paths.get("src\\chapter20\\dummy.txt");
        Path pathToDummyCopy = Paths.get("src\\chapter20\\dummyChapter\\dummy.txt");

        try {
            Files.move(pathToDummy, pathToDummyCopy, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //please move file back after running this test
    }

    @Test
    void testDeleteAndDeleteIfExistsMethodsOnFiles() {
        Path pathToDirectory = Paths.get("src\\chapter20\\dummyChapter");

        //directory should be empty when trying to delete - otherwise will throw exception
        assertThrows(IOException.class, () -> Files.delete(pathToDirectory));

        //delete throws exception if path doesn't exist
        assertThrows(IOException.class, () -> Files.delete(Path.of("dummy\\path")));

        //deleteIfExists returns false if path doesn't exist
        try {
            assertFalse(Files.deleteIfExists(Path.of("dummy\\path")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testReadingAndWritingDataWithBufferedStreamsOnFiles() {
        Path readFromFile = Paths.get("src\\chapter20\\dummy.txt");
        String line;
        try(BufferedReader br = Files.newBufferedReader(readFromFile)) {
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //StandardOpenOption provides 6 values
        final var append = StandardOpenOption.APPEND;
        final var create = StandardOpenOption.CREATE;
        final var createNew = StandardOpenOption.CREATE_NEW;
        final var read = StandardOpenOption.READ;
        final var truncateExisting = StandardOpenOption.TRUNCATE_EXISTING;
        final var write = StandardOpenOption.WRITE;

        Path writeToFile = Paths.get("src\\chapter20\\dummyChapter\\dummy.txt");
        try(BufferedWriter writer = Files.newBufferedWriter(writeToFile, StandardOpenOption.APPEND)) {
            List<String> linesToAppend = List.of("first appended line", "second line", "third line");
            for (String s : linesToAppend) {
                writer.newLine();
                writer.write(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void testReadAllLinesMethodOnFiles() {
        Path path = Paths.get("src\\chapter20\\dummyChapter\\dummy.txt");

        try {
            Files.readAllLines(path)
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
