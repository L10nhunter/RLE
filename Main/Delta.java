public class Delta {
    protected static Sprite encode(Sprite plane) {
        Sprite deltaPlane = new Sprite(plane.width, plane.height, true);
        byte lastBit = 0;
        for(int i = 0; i < plane.image.getHeight(); i++){
            for(int j = 0; j < plane.image.getWidth(); j++){
                final byte currentBit = plane.getPixelFromBitStream(j,i);
                if(currentBit == lastBit) deltaPlane.updatePlane(j, i, Sprite.pixelToRGB[0], (byte)0);
                else deltaPlane.updatePlane(j, i, Sprite.pixelToRGB[3], (byte)1);
                lastBit = currentBit;
            }
        }
        deltaPlane.updateFromBitStream(true);
        return deltaPlane;
    }
    protected static Sprite decode(Sprite deltaEncodedPlane) {
        Sprite deltaDecodedPlane = new Sprite(deltaEncodedPlane.width, deltaEncodedPlane.height, true);
        byte lastColor = 0;
        for(int i = 0; i < deltaEncodedPlane.image.getHeight(); i++){
            for(int j = 0; j < deltaEncodedPlane.image.getWidth(); j++){
                final byte currentBit = deltaEncodedPlane.getPixelFromBitStream(j,i);
                if(currentBit == 1) lastColor = (byte)(lastColor ^ 1);
                deltaDecodedPlane.updatePlane(j, i, Sprite.pixelToRGB[lastColor<<1 | lastColor], lastColor);
            }
        }
        deltaDecodedPlane.updateFromBitStream(true);
        return deltaDecodedPlane;
    }
}
