import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class MainTests {
    public static String mon = "Gengar";
    public static BufferedImage image;
    public static Sprite sprite, mergeSprite;
    public static Sprite[] bitPlane, DeltaSprite, DeltaDecoded, RLESprite, RLEDecoded;
    public static File MonSprite;
    public static File MonMSBPlane;
    public static File MonLSBPlane;
    public static File MonMSBPlaneDeltaEncoded;
    public static File MonLSBPlaneDeltaEncoded;
    public static File MonZipperedPlaneDeltaEncoded;
    public static File MonMSBPlaneRLEEncoded;
    public static File MonLSBPlaneRLEEncoded;
    public static File MonZipperedRLEEncoded;
    public static File MonMSBPlaneRLEDecoded;
    public static File MonLSBPlaneRLEDecoded;
    public static File MonZipperedRLEDecoded;
    public static File MonMSBPlaneDeltaDecoded;
    public static File MonLSBPlaneDeltaDecoded;
    public static File MonMerged;

    static {
        try {
            image = ImageIO.read(new File("Tests/Images/" + mon + "/" + mon + ".png"));
            MonSprite = new File("Tests/Images/" + mon + "/" + mon + "Sprite.png");
            MonMSBPlane = new File("Tests/Images/" + mon + "/" + mon + "MSB.png");
            MonLSBPlane = new File("Tests/Images/" + mon + "/" + mon + "LSB.png");
            MonMSBPlaneDeltaEncoded = new File("Tests/Images/" + mon + "/" + mon + "MSBDeltaEncode.png");
            MonLSBPlaneDeltaEncoded = new File("Tests/Images/" + mon + "/" + mon + "LSBDeltaEncode.png");
            MonZipperedPlaneDeltaEncoded = new File("Tests/Images/" + mon + "/" + mon + "ZipperedDeltaEncode.png");
            MonMSBPlaneRLEEncoded = new File("Tests/Images/" + mon + "/" + mon + "MSBRLEEncode.png");
            MonLSBPlaneRLEEncoded = new File("Tests/Images/" + mon + "/" + mon + "LSBRLEEncode.png");
            MonZipperedRLEEncoded = new File("Tests/Images/" + mon + "/" + mon + "ZipperedRLEEncoded.png");
            MonMSBPlaneRLEDecoded = new File("Tests/Images/" + mon + "/" + mon + "MSBRLEDecode.png");
            MonLSBPlaneRLEDecoded = new File("Tests/Images/" + mon + "/" + mon + "LSBRLEDecode.png");
            MonZipperedRLEDecoded = new File("Tests/Images/" + mon + "/" + mon + "ZipperedRLEDecoded.png");
            MonMSBPlaneDeltaDecoded = new File("Tests/Images/" + mon + "/" + mon + "MSBDeltaDecode.png");
            MonLSBPlaneDeltaDecoded = new File("Tests/Images/" + mon + "/" + mon + "LSBDeltaDecode.png");
            MonMerged = new File("Tests/Images/" + mon + "/" + mon + "Merged.png");
        } catch (IOException e) {
            Print.error("Image not found");
        }
    }

    private static final String[] channelStrings = {"Blue ", "Green", "Red  ", "Alpha"};

    public static void main(String[] args) throws IOException {
        runTestSuite();
    }

    public static void runTestSuite() throws IOException {
        Main.main(new String[]{"debug", "RLEEncodeDebug", "RLEDecodeDebug", "verbose4"});

        System.out.println("Running Test Suite\n");

        if (Main.compareDelta) {
            System.out.println("Delta MSB Test:");
            if (Main.verbose == 6 || (Main.verbose > 6 && Main.verbose % 2 == 1))
                Print.inLines(Main.deltaDecodedPlanes[0]);
            testBitStream(Main.splitPlanes[0], Main.deltaDecodedPlanes[0]);
            System.out.println("Delta LSB Test:");
            if (Main.verbose == 6 || (Main.verbose > 6 && Main.verbose % 2 == 1))
                Print.inLines(Main.deltaDecodedPlanes[1]);
            testBitStream(Main.splitPlanes[1], Main.deltaDecodedPlanes[1]);
        }

        if (!Main.noRLE && Main.compareRLE) {
            System.out.println("RLE MSB Test:");
            if (Main.verbose == 6 || (Main.verbose > 6 && Main.verbose % 2 == 1))
                Print.inLines(Main.RLEDecodedPlanes[0]);
            testBitStream(Main.deltaEncodedPlanes[0], Main.RLEDecodedPlanes[0]);
            System.out.println("RLE LSB Test:");
            if (Main.verbose == 6 || (Main.verbose > 6 && Main.verbose % 2 == 1))
                Print.inLines(Main.RLEDecodedPlanes[1]);
            testBitStream(Main.deltaEncodedPlanes[1], Main.RLEDecodedPlanes[1]);
        }

        if (Main.compareMerge) {
            System.out.println("Merge Test:");
            if (Main.verbose == 6 || (Main.verbose > 6 && Main.verbose % 2 == 1)) Print.inLines(Main.monSprite);
            testBitStream(Main.monSprite, Main.planesMerged);
        }
    }

    public static void testPrep() {
        Main.test = true;
        sprite = new Sprite(image, false);
        bitPlane = sprite.splitPlanes();
        DeltaSprite = new Sprite[]{Delta.encode(bitPlane[0]), Delta.encode(bitPlane[1])};
        RLESprite = new Sprite[]{RLE.encode(DeltaSprite[0]), RLE.encode(DeltaSprite[1])};
        RLEDecoded = new Sprite[]{RLE.decode(RLESprite[0]), RLE.decode(RLESprite[1])};
        DeltaDecoded = new Sprite[]{Delta.decode(RLEDecoded[0]), Delta.decode(RLEDecoded[1])};
        mergeSprite = Sprite.mergePlanes(DeltaDecoded);
        Print.tabReset();
    }

    public static boolean testWidth(Sprite sprite1, BufferedImage image) {
        if (sprite1.width == image.getWidth()) {
            Print.pass("Pass");
            return true;
        }
        Print.error("Fail");
        Print.error("Expected: " + image.getWidth() + " Actual: " + sprite1.width);
        return false;
    }
    public static boolean testWidth(Sprite sprite1, Sprite sprite2) {
        return testWidth(sprite1, sprite2.image);
    }
    public static boolean testWidth(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "Width Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testWidth(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testWidth(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }

    public static boolean testHeight(Sprite sprite1, BufferedImage image) {
        if (sprite1.height == image.getHeight()) {
            Print.pass("Pass");
            return true;
        }
        Print.error("Fail");
        Print.error("Expected: " + image.getHeight() + " Actual: " + sprite1.height);
        return false;
    }
    public static boolean testHeight(Sprite sprite1, Sprite sprite2) {
        return testHeight(sprite1, sprite2.image);
    }
    public static boolean testHeight(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "Height Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testHeight(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testHeight(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }

    public static boolean testPlane(Sprite sprite1, Sprite sprite2) {
        if (sprite1.isPlane == sprite2.isPlane) {
            Print.pass("Pass");
            return true;
        }
        Print.error("Fail");
        Print.error("Expected: " + sprite2.isPlane + " Actual: " + sprite1.isPlane);
        return false;
    }
    public static boolean testPlane(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "isPlane Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testPlane(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testPlane(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }

    public static boolean testARGB(Sprite sprite1, BufferedImage image) {
        if(Arrays.equals(sprite1.image.getRGB(0, 0, sprite1.width, sprite1.height, null, 0, sprite1.width), image.getRGB(0, 0, sprite1.width, sprite1.height, null, 0, sprite1.width))) {
            Print.pass("Pass");
            return true;
        }
        else {
            StringBuilder out = new StringBuilder();
            Print.tabUp();
            for (int i = 3; i >= 0; i--) compareColorChannel(sprite1, image, i, out);
            System.out.println("\n" + out);
            Print.tabDown();
            return false;
        }
    }
    public static boolean testARGB(Sprite sprite1, Sprite sprite2) {
        return testARGB(sprite1, sprite2.image);
    }
    public static boolean testARGB(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "RGB Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testARGB(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testARGB(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }
    private static void compareColorChannel(Sprite sprite1, BufferedImage image, int channel, StringBuilder out) {

        int errorCount = 0;
        int i = 0, j = 0;
        while (i < sprite1.image.getHeight()) {
            while (j < sprite1.image.getWidth()) {
                int normal = (((sprite1.image.getRGB(j, i) & 0xff << ((channel - 1) * 8)) >> (channel - 1) * 8) - ((image.getRGB(j, i) & 0xff << ((channel - 1) * 8)) >> (channel - 1) * 8));
                int error = 0;
                if (normal != 0) error = (normal) / Math.abs(normal);
                switch (error) {
                    case 0 -> out.append("0 ");
                    case 1 -> {
                        out.append(Print.errorString("1 "));
                        errorCount++;
                    }
                    case -1 -> {
                        out.append(Print.warningString("1 "));
                        errorCount++;
                    }
                    default -> {
                        out.append(Print.errorString("2 "));
                        errorCount++;
                    }
                }
                j++;
            }
            out.append("\n");
            j = 0;
            i++;

        }
        if (errorCount == 0)
            System.out.println(Print.tabString() + channelStrings[channel] + " Test: " + Print.passString("Pass"));
        else {
            System.out.println("\n" + out);
            System.out.println(Print.tabString() + channelStrings[channel] + " Test: " + Print.errorString("Fail"));
            Print.error(Print.tabString() + channelStrings[channel] + " Error Count: " + errorCount + "\n");
        }
    }

    public static boolean testImage(Sprite sprite1, BufferedImage image) {
        if(Arrays.equals(sprite1.image.getRGB(0, 0, sprite1.width, sprite1.height, null, 0, sprite1.width), image.getRGB(0, 0, sprite1.width, sprite1.height, null, 0, sprite1.width))) {
            Print.pass("Pass");
            return true;
        }
        Print.error("Fail");
        Print.error("Expected: " + image.getData().toString() + "\nActual: " + sprite1.image.getData().toString());
        return false;
    }
    public static boolean testImage(Sprite sprite1, Sprite sprite2) {
        return testImage(sprite1, sprite2.image);
    }
    public static boolean testImage(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "Image Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testImage(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testImage(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }

    public static boolean testBitStream(Sprite sprite1, Sprite sprite2) {
        StringBuilder out = new StringBuilder();
        int errorCount = 0;
        for (int i = 0; i < sprite1.image.getWidth(); i++) {
            for (int j = 0; j < sprite1.image.getHeight(); j++) {
                int error = sprite1.getPixelFromBitStream(i, j) - (sprite2.getPixelFromBitStream(i, j));
                if (error != 0) error /= Math.abs(error);
                switch (error) {
                    case 0 -> out.append("0 ");
                    case 1 -> {
                        out.append(Print.errorString("1")).append(" ");
                        errorCount++;
                    }
                    case -1 -> {
                        out.append(Print.warningString("1")).append(" ");
                        errorCount++;
                    }
                    default -> {
                        System.out.println(error + " " + sprite1.getPixelFromBitStream(j, i) + " " + sprite2.getPixelFromBitStream(j, i));
                        out.append(Print.errorString("2")).append(" ");
                        errorCount++;
                    }
                }
            }
            out.append("\n");
        }
        if (errorCount == 0) {
            Print.pass("Pass");
            return true;
        } else {
            System.out.println("\n" + out);
            Print.error(Print.tabString() + "BitStream Error Test: Fail");
            Print.error(String.format("%s%s %d,%n%s %d,%n%s %d", Print.tabString(), "Expected zeros:", RLE.actualZeroCount, "Encoded zeros:", RLE.encodeZeroCount, "Decoded zeros:", RLE.decodeZeroCount));
            Print.error(String.format("%s%s %d,%n%s %d,%n%s %d", Print.tabString(), "Expected Ones:", RLE.actualOneCount, "Encoded zeros:", RLE.encodeOneCount, "Decoded zeros:", RLE.decodeOneCount));
            Print.error(Print.tabString() + "BitStream Error Count: " + errorCount + "\n");
            return false;
        }
    }
    public static boolean testBitStream(Sprite[] sprite1, Sprite[] sprite2) {
        Print.bold(Print.tabString() + "BitStream Test:\n");
        System.out.print(Print.tabUp() + "MSB Test: ");
        boolean MSB = testBitStream(sprite1[0], sprite2[0]);
        System.out.print(Print.tabString() + "LSB Test: ");
        boolean LSB = testBitStream(sprite1[1], sprite2[1]);
        Print.tabDown();
        return MSB && LSB;
    }


}
