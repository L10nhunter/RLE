import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import javax.imageio.ImageIO;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RLETest {

    @org.junit.jupiter.api.BeforeAll
    static void testPrep() {
        MainTests.testPrep();
        Print.header(Print.tabString() + "RLE Tests:");
        Print.tabUp();
    }

    @org.junit.jupiter.api.Test @Order(1)
    void testWidth() {
        assertTrue(MainTests.testWidth(MainTests.DeltaSprite, MainTests.RLEDecoded));
    }
    @org.junit.jupiter.api.Test @Order(2)
    void testHeight() {
        assertTrue(MainTests.testHeight(MainTests.DeltaSprite, MainTests.RLEDecoded));
    }
    @org.junit.jupiter.api.Test @Order(3)
    void testIsPlane() {
        assertTrue(MainTests.testPlane(MainTests.DeltaSprite, MainTests.RLEDecoded));
    }
    @org.junit.jupiter.api.Test @Order(4)
    void testBitStream() {
        assertTrue(MainTests.testBitStream(MainTests.DeltaSprite, MainTests.RLEDecoded));
        System.out.println(Print.tabUp() + "MSB BitStream Length: " + MainTests.RLESprite[0].bitStream.length());
        System.out.println(Print.tabDown() + "LSB BitStream Length: " + MainTests.RLESprite[1].bitStream.length());
    }
    @org.junit.jupiter.api.Test @Order(5)
    void testImage() {
        assertTrue(MainTests.testImage(MainTests.DeltaSprite, MainTests.RLEDecoded));
    }
    @org.junit.jupiter.api.Test @Order(6)
    void testRGB() {
        assertTrue(MainTests.testARGB(MainTests.DeltaSprite, MainTests.RLEDecoded));
    }
    @org.junit.jupiter.api.AfterAll

    static void tearDown() throws IOException {
        ImageIO.write(MainTests.DeltaSprite[0].image, "png", MainTests.MonMSBPlaneDeltaEncoded);
        ImageIO.write(MainTests.RLEDecoded[0].image, "png", MainTests.MonMSBPlaneRLEEncoded);
        ImageIO.write(MainTests.RLEDecoded[0].image, "png", MainTests.MonMSBPlaneRLEDecoded);
        ImageIO.write(MainTests.DeltaSprite[1].image, "png", MainTests.MonLSBPlaneDeltaEncoded);
        ImageIO.write(MainTests.RLEDecoded[1].image, "png", MainTests.MonLSBPlaneRLEEncoded);
        ImageIO.write(MainTests.RLEDecoded[1].image, "png", MainTests.MonLSBPlaneRLEDecoded);
        Print.tabReset();
        Print.bold("RLE Tests Complete\n\n");
    }
}
