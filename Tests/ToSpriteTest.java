import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import javax.imageio.ImageIO;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ToSpriteTest {
    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
        MainTests.testPrep();
        Print.header(Print.tabString() + "Sprite Tests:");
        Print.tabUp();
    }

    @org.junit.jupiter.api.Test @Order(1)
    void testWidth() {
        Print.bold(Print.tabString() + "Width Test:     ");
        assertTrue(MainTests.testWidth(MainTests.sprite, MainTests.image));
    }
    @org.junit.jupiter.api.Test @Order(2)
    void testHeight() {
        Print.bold(Print.tabString() + "Height Test:    ");
        assertTrue(MainTests.testHeight(MainTests.sprite, MainTests.image));
    }
    @org.junit.jupiter.api.Test @Order(3)
    void testisPlane() {
        Print.bold(Print.tabString() + "isPlane Test:   ");
        assertFalse(MainTests.sprite.isPlane);
        Print.pass("Pass");
    }
    @org.junit.jupiter.api.Test @Order(4)
    void testBitStream() {
        Print.bold(Print.tabString() + "BitStream Test: ");
        for (int i = 0; i < MainTests.sprite.width; i++) {
            for (int j = 0; j < MainTests.sprite.height; j++) {
                assertEquals(Sprite.pixelToRGB[MainTests.sprite.getPixelFromBitStream(i, j)], MainTests.image.getRGB(i, j), String.format("%d %d: %h %h%n", i, j, Sprite.pixelToRGB[MainTests.sprite.getPixelFromBitStream(i, j)], MainTests.image.getRGB(i, j)));
            }
        }
        Print.pass("Pass");
    }
    @org.junit.jupiter.api.Test @Order(5)
    void testImage() {
        Print.bold(Print.tabString() + "Image Test:     ");
        assertTrue(MainTests.testImage(MainTests.sprite, MainTests.image));
    }
    @org.junit.jupiter.api.Test @Order(6)
    void testRGB() {
        Print.bold(Print.tabString() + "RGB Test:       ");
        assertTrue(MainTests.testARGB(MainTests.sprite, MainTests.image));
    }

    @org.junit.jupiter.api.AfterAll
    static void tearDown() throws IOException {
        ImageIO.write(MainTests.sprite.image, "png", MainTests.MonSprite);
        Print.tabReset();
        Print.bold("Sprite Tests Complete\n\n");
    }

}
