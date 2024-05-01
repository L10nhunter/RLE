import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static BufferedImage monImage;
    static Sprite[] splitPlanes, deltaEncodedPlanes, RLEPlanes, RLEDecodedPlanes, deltaDecodedPlanes;
    static Sprite monSprite, planesMerged;
    static boolean test = false;
    static boolean RLEEncodeDebug = false;
    static boolean showDataLength = true;
    static boolean compareDelta = false;
    static boolean testSuite = false;
    static boolean noRLE = false;
    static boolean noDelta = false;
    static boolean noDecode = false;
    static boolean onlyBP0 = false;
    static boolean onlyBP1 = false;
    static boolean RLEDecodeDebug = false;
    static boolean compareRLE = false;
    static boolean compareMerge = false;
    static byte encodeMode = 1;
    static byte bp0 = 0;
    static int verbose = 1;
    static int RLEDataSize = 0;
    static int bp0Size = 0;
    static int bp1Size = 0;
    static boolean mewtwo = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("debug")) {
            for (int i = 1; i < args.length; i++) {
                if(args[i].contains("verbose") && args[i].length() > 7) {
                    verbose = Integer.parseInt(args[i].substring(7));
                    continue;
                }
                switch (args[i]) {
                    case "RLEEncodeDebug" -> RLEEncodeDebug = true;
                    case "compareDelta" -> compareDelta = true;
                    case "testSuite" -> testSuite = true;
                    case "noRLE" -> noRLE = true;
                    case "noDelta" -> noDelta = true;
                    case "noDecode" -> noDecode = true;
                    case "onlyBP0" -> onlyBP0 = true;
                    case "onlyBP1" -> onlyBP1 = true;
                    case "RLEDecodeDebug" -> RLEDecodeDebug = true;
                    case "compareRLE" -> compareRLE = true;
                    case "compareMerge" -> compareMerge = true;
                    case "all" -> {
                        RLEEncodeDebug = true;
                        testSuite = true;
                        RLEDecodeDebug = true;
                        compareMerge = true;
                    }
                    default -> {
                        System.err.println("Invalid debug argument: " + args[i]);
                        System.exit(1);
                    }
                }
            }
        }
        //for(String arg : args) System.out.println(arg);
        while(encodeMode <=3){
            while(bp0 <= 1){
                encodeAndDecode("Gengar");
                bp0++;
            }
            bp0 = 0;
            encodeMode++;
        }
        encodeMode = 1;
        bp0 = 0;
        if (mewtwo) {
            while(encodeMode <=3){
                while(bp0 <= 1){
                    encodeAndDecode("Mewtwo");
                    bp0++;
                }
                encodeMode++;
            }
        }
    }

    private static void encodeAndDecode(String mon) throws IOException {
        
        monImage = ImageIO.read(new File("Main/Images/" + mon + "/" + mon + ".png"));
        File MonSprite = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "Sprite.png");
        File MonBP0Plane = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP0.png");
        File MonBP1Plane = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP1.png");
        File MonBP0PlaneDeltaEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP0DeltaEncode.png");
        File MonBP1PlaneDeltaEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP1DeltaEncode.png");
        File MonZipperedPlaneDeltaEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "ZipperedDeltaEncode.png");
        File MonBP0PlaneRLEEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP0RLEEncode.png");
        File MonBP1laneRLEEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP1RLEEncode.png");
        File MonZipperedRLEEncoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "ZipperedRLEEncoded.png");
        File MonBP0PlaneRLEDecoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP0RLEDecode.png");
        File MonBP1PlaneRLEDecoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP1RLEDecode.png");
        File MonZipperedRLEDecoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "ZipperedRLEDecoded.png");
        File MonBP0PlaneDeltaDecoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP0DeltaDecode.png");
        File MonBP1PlaneDeltaDecoded = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "BP1DeltaDecode.png");
        File MonMerged = new File("Main/Images/" + mon + "/" + bp0 + "/" + encodeMode + "/" + mon + "Merged.png");

        System.out.println("Encoding and Decoding " + mon + " with BP0: " + bp0 + " and Encode Mode: " + encodeMode);

        //Turn input into Sprite
        if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Converting to Sprite...");
        monSprite = new Sprite(monImage, false);
        ImageIO.write(monSprite.image, "png", MonSprite);
        if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("Converting to Sprite Complete\n");


        // Split the planes
        if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Splitting Planes...");
        splitPlanes = monSprite.splitPlanes();
        if(bp0 == 1) splitPlanes = new Sprite[]{splitPlanes[1], splitPlanes[0]};
        if(encodeMode == 2 || encodeMode == 3) splitPlanes[1] = XOR.encode(splitPlanes[1], splitPlanes[0]);
        if(!onlyBP1) ImageIO.write(splitPlanes[0].image, "png", MonBP0Plane);
        if(!onlyBP0) ImageIO.write(splitPlanes[1].image, "png", MonBP1Plane);
        if (verbose  == 2  || (verbose > 2 && verbose % 2 == 1)) Print.pass("Splitting Planes Complete\n");



        if (!noDelta) {
            // Delta Encode the planes
            if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Delta Encode Planes...");

            deltaEncodedPlanes = new Sprite[2];
            if(!onlyBP1) {

                    deltaEncodedPlanes[0] = Delta.encode(splitPlanes[0]);
                    ImageIO.write(deltaEncodedPlanes[0].image, "png", MonBP0PlaneDeltaEncoded);
            }
            if(!onlyBP0) {
                if (encodeMode == 1 || encodeMode == 3) {
                    deltaEncodedPlanes[1] = Delta.encode(splitPlanes[1]);
                    ImageIO.write(deltaEncodedPlanes[1].image, "png", MonBP1PlaneDeltaEncoded);
                }
                else {
                    deltaEncodedPlanes[1] = splitPlanes[1];
                    ImageIO.write(deltaEncodedPlanes[1].image, "png", MonBP1PlaneDeltaEncoded);
                }
            }
            if(!onlyBP0 && !onlyBP1) ImageIO.write(Sprite.mergePlanes(deltaEncodedPlanes).image, "png", MonZipperedPlaneDeltaEncoded);
            if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("Delta Encode Planes Complete\n");
            //RLE Encode the planes.
            if (!noRLE) {
                if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("RLE Encode Planes...");
                RLEPlanes = new Sprite[2];
                if(!onlyBP1) {
                    RLEPlanes[0] = RLE.encode(deltaEncodedPlanes[0]);
                    bp0Size = RLEDataSize;
                    ImageIO.write(RLEPlanes[0].image, "png", MonBP0PlaneRLEEncoded);
                }
                if(!onlyBP0) {
                    RLEPlanes[1] = RLE.encode(deltaEncodedPlanes[1]);
                    bp1Size = RLEDataSize;
                    ImageIO.write(RLEPlanes[1].image, "png", MonBP1laneRLEEncoded);
                }
                if(!onlyBP0 && !onlyBP1) ImageIO.write(new Sprite(RLEPlanes).image, "png", MonZipperedRLEEncoded);
                if(showDataLength){
                    System.out.println(" BP0  RLE Data Size | " + bp0Size + "b | " + bp0Size/8 + "B");
                    System.out.println(" BP1  RLE Data Size | " + bp1Size + "b | " + bp1Size/8 + "B");
                    System.out.println("Total RLE Data Size | " + (bp0Size + bp1Size) + "b | " + (bp0Size + bp1Size)/8 + "B");
                }
                if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("RLE Encode Planes Complete\n");

                if(!noDecode) {
                    //RLE Decode the planes.
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("RLE Decode Planes...");
                    RLEDecodedPlanes = new Sprite[2];
                    if(!onlyBP1) {
                        RLEDecodedPlanes[0] = RLE.decode(RLEPlanes[0]);
                        ImageIO.write(RLEDecodedPlanes[0].image, "png", MonBP0PlaneRLEDecoded);
                    }
                    if(!onlyBP0) {
                        RLEDecodedPlanes[1] = RLE.decode(RLEPlanes[1]);
                        ImageIO.write(RLEDecodedPlanes[1].image, "png", MonBP1PlaneRLEDecoded);
                    }
                    if(!onlyBP0 && !onlyBP1) ImageIO.write(Sprite.mergePlanes(RLEDecodedPlanes).image, "png", MonZipperedRLEDecoded);
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("RLE Decode Planes Complete\n");

                    // Delta Decode the planes.
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Delta Decode Planes...");
                    deltaDecodedPlanes = new Sprite[2];
                    if(!onlyBP1) {
                        deltaDecodedPlanes[0] = Delta.decode(RLEDecodedPlanes[0]);
                        ImageIO.write(deltaDecodedPlanes[0].image, "png", MonBP0PlaneDeltaDecoded);
                    }
                    if(!onlyBP0) {
                        if(encodeMode == 1 || encodeMode == 3) deltaDecodedPlanes[1] = Delta.decode(RLEDecodedPlanes[1]);
                        else deltaDecodedPlanes[1] = RLEDecodedPlanes[1];
                        if(encodeMode == 2 || encodeMode == 3) deltaDecodedPlanes[1] = XOR.decode(deltaDecodedPlanes[1], deltaDecodedPlanes[0]);
                        ImageIO.write(deltaDecodedPlanes[1].image, "png", MonBP1PlaneDeltaDecoded);
                    }
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("Delta Decode Planes Complete\n");
                }
                else deltaDecodedPlanes = splitPlanes;
            }
            else {
                if(!noDecode) {
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Delta Decode Planes...");
                    deltaDecodedPlanes = new Sprite[2];
                    if(!onlyBP1) {
                        Delta.decode(deltaEncodedPlanes[0]);
                        ImageIO.write(deltaDecodedPlanes[0].image, "png", MonBP0PlaneDeltaDecoded);
                    }
                    if(!onlyBP0) {
                        if(encodeMode == 1 || encodeMode == 3) deltaDecodedPlanes[1] = Delta.decode(RLEDecodedPlanes[1]);
                        if(encodeMode == 2 || encodeMode == 3) deltaDecodedPlanes[1] = XOR.decode(deltaEncodedPlanes[1], deltaDecodedPlanes[0]);
                        ImageIO.write(deltaDecodedPlanes[1].image, "png", MonBP1PlaneDeltaDecoded);
                    }
                    if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("Delta Decode Planes Complete\n");
                }
            }
        }
        else {
            deltaDecodedPlanes = splitPlanes;
        }

        // Merge the planes
        if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) System.out.println("Merging Planes...");
        if(!onlyBP0 && !onlyBP1) {
            planesMerged = Sprite.mergePlanes(deltaDecodedPlanes);
            ImageIO.write(planesMerged.image, "png", MonMerged);
        }
        if (verbose == 2 || (verbose > 2 && verbose % 2 == 1)) Print.pass("Merging Planes Complete\n");

        Print.pass("Finished Encoding and Decoding " + mon + " with BP0: " + bp0 + " and Encode Mode: " + encodeMode + "\n");
    }


}
