package chapter19;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommonIOStreamsMethods {

    @Test
    void testCloseAndReadMethodOnReaderClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedReader reader = new BufferedReader(new FileReader(resource.getPath()));
        //FileReader throws checked FileNotFound exception which implements IOException
        int b;
        while((b = reader.read()) != -1) {      // -1 special value that specifies stream end
            System.out.print((char) b);     //print each character of test.txt
        }

        reader.close();     //closes the stream. Throws checked IOException. Exists on all types of stream
    }

    @Test
    void testReadMethodWithBytesOnInputStreamClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resource.getFile()));

        int b;
        byte[] bytes = new byte[10];
        while((b = bis.read(bytes)) != -1) {        //the bytes array serves as a buffer. At each point we read a fixed amount
                                                    //from stream, in our case it is 10 because that's the length of buffer.
                                                    //If in the end the characters in the stream are less than byte array length
                                                    //it will populate the array with all left characters and will not remove
                                                    // the old values from the previous iteration.
            for (int i = 0; i <10; i++) {
                System.out.println((char) bytes[i]);
            }
            // After running the program you should see that same characters at the end will repeat.
        }
    }

    @Test
    void testReadMethodWithBytesOnReaderClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedReader reader = new BufferedReader(new FileReader(resource.getPath()));

        int b;
        char[] bytes = new char[10];
        while((b = reader.read(bytes)) != -1) {        //same as method before only in this case it is char instead of bytes
            for (int i = 0; i <10; i++) {
                System.out.println((char) bytes[i]);
            }
            // After running the program you should see that same characters at the end will repeat.
        }
    }

    @Test
    void testReadMethodWithBytesAndOffsetOnInputStreamClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resource.getFile()));

        int b;
        byte[] bytes = new byte[10];
        while((b = bis.read(bytes, 3, 2)) != -1) {      //Starts to write in the array from the off (offset)
                                                                //at each calls reads only len (length) number of bytes from stream
            for (int i = 0; i <10; i++) {
                System.out.println((char) bytes[i]);            //based on the read above, the array will always have only the 3rd and 4th index
                                                                //(it starts from offset 3 and reads 2 values - index 3 and 4)
                                                                //because of this, there will be a lot of 0s transformed to char
            }
        }
    }

    @Test
    void testWriteMethodOnOutputStreamClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("output.txt");
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resource.getFile()));

        String text = "This text was sent from testWriteMethodOnOutputStreamClass test method in TestCommonIOStreamsMethods class";

        for (int i = 0; i < text.length(); i++) {
            bos.write(text.charAt(i));
        }

        bos.close();
    }

    @Test
    void testWriteMethodOnWriterClass() throws IOException {
        URL resource = getClass().getClassLoader().getResource("output.txt");
        BufferedWriter bos = new BufferedWriter(new FileWriter(resource.getFile()));

        String text = "This text was sent from testWriteMethodOnOutputStreamClass test method in TestCommonIOStreamsMethods class";
        final var subs = text.split(" ");
        for (int i = 0; i < subs.length; i++) {
            char[] charSubs = new char[subs[i].length() + 1];
            for (int j = 0; j < subs[i].length(); j++) {
                charSubs[j] = subs[i].charAt(j);
            }
            charSubs[subs[i].length()] = ' ';
            bos.write(charSubs);
        }

        bos.close();
    }

    @Test
    void testMarkSupportedOnStream() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resource.getFile()));

        assertTrue(bis.markSupported());
    }

    @Test
    void testMarkAndResetOnStream() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resource.getFile()));

        int b;
        int index = 1;
        while((b = bis.read()) != -1) {
            if (index == 10 && bis.markSupported()) {
                bis.mark(index);
            }
            if (index == 20) {
                bis.reset();
            }
            System.out.print((char) b);
            index++;
        }

        bis.close();
    }

    @Test
    void testSkipOnStream() throws IOException {
        URL resource = getClass().getClassLoader().getResource("test.txt");
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(resource.getFile()));

        int b;
        int index = 0;
        long k = 0;
        while((b = bis.read()) != -1) {
            if (index == 415) {
                k = bis.skip(8);
            }
            index++;
            System.out.print((char) b);
        }
        System.out.print("\nNr of skiped bytes: " + k);
        bis.close();
    }
}
