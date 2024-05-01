import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import javax.imageio.ImageIO;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SplitAndMergeTest {
    @org.junit.jupiter.api.BeforeAll
    static void setUp(){
        Print.header("Merge Tests:");
        MainTests.testPrep();
        Print.tabUp();
    }

    @org.junit.jupiter.api.Test @Order(1)
    void testWidth() {
        Print.bold(Print.tabString() + "Width Test:     ");
        assertTrue(MainTests.testWidth(MainTests.sprite, MainTests.mergeSprite));
    }
    @org.junit.jupiter.api.Test @Order(2)
    void testHeight() {
        Print.bold(Print.tabString() + "Height Test:    ");
        assertTrue(MainTests.testHeight(MainTests.sprite, MainTests.mergeSprite));
    }
    @org.junit.jupiter.api.Test @Order(3)
    void testIsPlane() {
        Print.bold(Print.tabString() + "isPlane Test:   ");
        assertTrue(MainTests.testPlane(MainTests.sprite, MainTests.mergeSprite));
    }
    @org.junit.jupiter.api.Test @Order(4)
    void testBitStream() {
        Print.bold(Print.tabString() + "BitStream Test: ");
        assertTrue(MainTests.testBitStream(MainTests.sprite, MainTests.mergeSprite));
    }
    @org.junit.jupiter.api.Test @Order(5)
    void testImage() {
        Print.bold(Print.tabString() + "Image Test:     ");
        assertTrue(MainTests.testImage(MainTests.sprite, MainTests.mergeSprite));
    }
    @org.junit.jupiter.api.Test @Order(6)
    void testRGB() {
        Print.bold(Print.tabString() + "RGB Test:       ");
        assertTrue(MainTests.testARGB(MainTests.sprite, MainTests.mergeSprite));
    }

    @org.junit.jupiter.api.AfterAll
    static void tearDown() throws IOException {
        ImageIO.write(MainTests.mergeSprite.image, "png", MainTests.MonMerged);
        Print.bold("Merge Tests Complete\n\n");
    }
}
