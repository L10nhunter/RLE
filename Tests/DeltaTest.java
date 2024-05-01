import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

import javax.imageio.ImageIO;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeltaTest {
    @org.junit.jupiter.api.BeforeAll
    static void setUp() {
        MainTests.testPrep();
        Print.header("Delta Tests:");
        Print.tabUp();
    }

    @org.junit.jupiter.api.Test @Order(1)
    void testWidth() {
        assertTrue(MainTests.testWidth(MainTests.bitPlane, MainTests.DeltaDecoded));
    }
    @org.junit.jupiter.api.Test @Order(2)
    void testHeight() {
        assertTrue(MainTests.testHeight(MainTests.bitPlane, MainTests.DeltaDecoded));
    }
    @org.junit.jupiter.api.Test @Order(3)
    void testIsPlane() {
        assertTrue(MainTests.testPlane(MainTests.bitPlane, MainTests.DeltaDecoded));
    }
    @org.junit.jupiter.api.Test @Order(4)
    void testBitStream() {
        assertTrue(MainTests.testBitStream(MainTests.bitPlane, MainTests.DeltaDecoded));
    }
    @org.junit.jupiter.api.Test @Order(5)
    void testImage() {
        assertTrue(MainTests.testImage(MainTests.bitPlane, MainTests.DeltaDecoded));
    }
    @org.junit.jupiter.api.Test @Order(6)
    void testRGB() {
        assertTrue(MainTests.testARGB(MainTests.bitPlane, MainTests.DeltaDecoded));
    }

    @org.junit.jupiter.api.AfterAll
    static void tearDown() throws IOException {
        ImageIO.write(MainTests.bitPlane[0].image, "png", MainTests.MonMSBPlane);
        ImageIO.write(MainTests.DeltaDecoded[0].image, "png", MainTests.MonMSBPlaneDeltaDecoded);
        ImageIO.write(MainTests.bitPlane[1].image, "png", MainTests.MonLSBPlane);
        ImageIO.write(MainTests.DeltaDecoded[1].image, "png", MainTests.MonLSBPlaneDeltaDecoded);
        Print.tabReset();
        Print.bold("Delta Tests Complete\n\n");
    }
}
