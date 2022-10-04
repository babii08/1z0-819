package chapter20;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestFileAttributes {

    @Test
    void testIsDirectoryMethodOnFiles() {
        Path directory = Paths.get("src");
        assertTrue(Files.isDirectory(directory, LinkOption.NOFOLLOW_LINKS));

        Path file = Paths.get("src\\chapter20\\dummy.txt");
        assertFalse(Files.isDirectory(file, LinkOption.NOFOLLOW_LINKS));

    }

    @Test
    void testIsSymbolicLinkOnFiles() {
        //this method doesn't work as java doesn't have permission to create symbolicLinks.

        Path dummyChapter = Path.of("src\\chapter20\\dummyChapter");
        Path symbolicLinkToDummyChapter = Path.of("src\\chapter20\\symbolicLinkToDummyChapter");

        try {
            Path path = Files.createSymbolicLink(symbolicLinkToDummyChapter, dummyChapter);
            assertTrue(Files.isSymbolicLink(path));
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testIsRegularFileOnFiles() {
        Path dummyChapter = Path.of("src\\chapter20\\dummy.txt");

        assertTrue(Files.isRegularFile(dummyChapter));
    }

    @Test
    void testFileAccessibilityMethods() {
        Path dummyFile = Path.of("src\\chapter20\\dummy.txt");

        assertTrue(Files.isReadable(dummyFile));
        assertTrue(Files.isWritable(dummyFile));
        //I have no idea why dummy.txt is an executable file
        assertTrue(Files.isExecutable(dummyFile));
        try {
            assertFalse(Files.isHidden(dummyFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testSizeMethodOnFiles() {
        Path dummyFile = Path.of("src\\chapter20\\dummy.txt");
        Path dummyFolder = Path.of("src\\chapter20\\dummyChapter");

        try {
            System.out.println(Files.size(dummyFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //calling size method on directories is undefined
        try {
            System.out.println(Files.size(dummyFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testLastModifiedTime() {
        Path dummyFile = Path.of("src\\chapter20\\dummy.txt");

        try {
            System.out.println(Files.getLastModifiedTime(dummyFile).toInstant());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testReadAttributes() {
        Path dummyFile = Path.of("src\\chapter20\\dummy.txt");
        BasicFileAttributes bas = null;
        try {
            bas = Files.readAttributes(dummyFile,BasicFileAttributes.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Is a directory? " + bas.isDirectory());
        System.out.println("Is a regular file? " + bas.isRegularFile());
        System.out.println("Is a symbolic link? " + bas.isSymbolicLink());
        System.out.println("Size (in bytes): " + bas.size());
        System.out.println("Last modified: " + bas.lastModifiedTime());
    }

    @Test
    void testModifyingAttributesWithFileAttributeView() {
        Path dummyFile = Path.of("src\\chapter20\\dummy.txt");

        BasicFileAttributeView attributeView = Files.getFileAttributeView(dummyFile, BasicFileAttributeView.class);
        BasicFileAttributes bas;
        try {
            bas = attributeView.readAttributes();
            FileTime time = bas.lastAccessTime();
            attributeView.setTimes(null, time, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
