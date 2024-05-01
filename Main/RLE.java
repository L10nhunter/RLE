public class RLE {
    private static byte N = 0;
    public static int actualZeroCount = 0;
    public static int actualOneCount = 0;
    public static int encodeZeroCount = 0;
    public static int decodeZeroCount = 0;
    public static int encodeOneCount = 0;
    public static int decodeOneCount = 0;

    protected static Sprite encode(Sprite plane) {
        Sprite RLEPlane = new Sprite(plane.width, plane.height, true);

        //TEST CODE
        if (Main.test) {
            actualZeroCount = 0;
            actualOneCount = 0;
            for (int i = 0; i < plane.bitStream.length(); i += 2) {
                if (plane.extractFromBitStream(i, 2) == 0) actualZeroCount++;
                else actualOneCount++;
            }
        }

        RLEPlane.bitStream = new StringBuilder();
        int bitstreamIndex = 0;
        boolean RLEMode = false;
        //check if the first packet is RLE or data
        if (plane.getPixelFromBitStream(0, 0) == 0) {
            RLEPlane.bitStream.append("0");
            RLEMode = true;
        } else {
            RLEPlane.bitStream.append("1");
        }
        while (bitstreamIndex < plane.bitStream.length()) {
            byte nextValue = plane.extractFromBitStream(bitstreamIndex, 2);
            String nextString = String.format("%2s", Integer.toBinaryString(nextValue)).replace(' ', '0');
            if (RLEMode) {
                N++;
                // if in RLE mode, go through N = LV algorithm
                // if next two bits in the bitstream are not 00, add RLE encode N to RLEPlane, then switch to data mode
                if (nextValue != 0) {
                    GammaCoding(RLEPlane);
                    RLEMode = false;
                    bitstreamIndex -= 2;
                }
                // if next two bits in the bitstream are 00, add 1 to N
            } else {
                if (nextValue != 0) {
                    // if next two bits in the bitstream are not 00, add data to RLEPlane
                    RLEPlane.bitStream.append(nextString);
                    encodeOneCount++;
                } else {
                    // if in data mode, check if the next two bits are 00, if so switch to RLE mode
                    RLEMode = true;
                    N++;
                    RLEPlane.bitStream.append("00");
                }

            }
            bitstreamIndex += 2;
        }
        if (RLEMode && N > 0) {
            N++;
            GammaCoding(RLEPlane);
        }
        //DEBUG
        Main.RLEDataSize = RLEPlane.bitStream.length();
        RLEPlane.updateFromBitStream(true);
        return RLEPlane;
    }

    protected static Sprite decode(Sprite RLEPlane) {
        Sprite RLEDecodedPlane = new Sprite(RLEPlane.width, RLEPlane.height, true);
        RLEDecodedPlane.bitStream = new StringBuilder();
        // check if the first packet is RLE or data
        boolean RLEMode = RLEPlane.extractFromBitStream(0, 1) == 0;
        int bitStreamIndex = 1;
        while (bitStreamIndex < RLEPlane.bitStream.length()) {
            if (RLEMode) {
                // RLE decode the zeros
                StringBuilder LStr = new StringBuilder();
                while (RLEPlane.extractFromBitStream(bitStreamIndex++, 1) != 0) {
                    LStr.append("1");
                }
                LStr.append("0");
                byte V = RLEPlane.extractFromBitStream(bitStreamIndex, LStr.length());
                bitStreamIndex += LStr.length();
                byte N = (byte) (Byte.valueOf(LStr.toString(), 2) + V + 1);
                if (Main.RLEDecodeDebug)
                    System.out.printf("L: %6s, V: %6s, N: %2s%n", LStr, Integer.toBinaryString(V), N);
                RLEDecodedPlane.bitStream.append("00".repeat(N));
                decodeZeroCount += N;
                RLEMode = false;
            } else {
                // data decode the non zeros
                StringBuilder dataOut = new StringBuilder();
                while (bitStreamIndex < RLEPlane.bitStream.length() - 1 && RLEPlane.extractFromBitStream(bitStreamIndex, 2) != 0) {
                    byte nextString = RLEPlane.extractFromBitStream(bitStreamIndex, 2);
                    String replace = String.format("%2s", Integer.toBinaryString(nextString)).replace(' ', '0');
                    RLEDecodedPlane.bitStream.append(replace);
                    dataOut.append(replace).append(" ");
                    decodeOneCount++;
                    bitStreamIndex += 2;
                }
                if (Main.RLEDecodeDebug) System.out.println("DataOut: " + dataOut);
                RLEMode = true;
                bitStreamIndex += 2;
            }
        }
        RLEDecodedPlane.updateFromBitStream(true);
        return RLEDecodedPlane;
    }

    public static void GammaCoding(Sprite RLEPlane) {
        String NStr = String.format("%s", Integer.toBinaryString(N));
        //
        byte V = 0;
        String VStr = "0";
        if (N > 1) {
            V = Byte.valueOf(NStr.substring(1), 2);
            VStr = NStr.substring(1);
        }
        byte L = (byte) ((N - V) - 2);
        String LStr = String.format("%s", Integer.toBinaryString(L));
        String RLEStr = LStr + VStr;


        //DEBUG
        if (Main.RLEEncodeDebug) {
            System.out.printf("pairs: %2d | NBin: %7s | V: %6s | L: %6s | V+L+1: %2s | RLEStr: %12s%n", N - 1, String.format("%7s", Integer.toBinaryString(N - 1)), VStr, LStr, V + L + 1, RLEStr);
        }
        // RLE encode the zeros and add the next String to the bitstream
        RLEPlane.bitStream.append(RLEStr);

        // reset N and mode
        encodeZeroCount += N - 1;
        N = 0;
    }
}
