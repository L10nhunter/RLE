public class XOR {
    public static Sprite encode(Sprite target, Sprite mask) {
        Sprite xorPlane = new Sprite(target.width, target.height, true);
        for(int i = 0; i < target.image.getHeight(); i++){
            for(int j = 0; j < target.image.getWidth(); j++){
                final byte xor = target.getPixelFromBitStream(j, i) == mask.getPixelFromBitStream(j, i) ? (byte) 0 : (byte) 1;
                xorPlane.updatePlane(j, i, Sprite.pixelToRGB[(xor<<1) | xor], xor);
            }
        }
        return xorPlane;
    }
    public static Sprite decode(Sprite xorPlane, Sprite mask) {
        Sprite decodedPlane = new Sprite(xorPlane.width, xorPlane.height, true);
        for(int i = 0; i < xorPlane.image.getHeight(); i++){
            for(int j = 0; j < xorPlane.image.getWidth(); j++){
                final byte xor = xorPlane.getPixelFromBitStream(j, i);
                decodedPlane.updatePlane(j, i, Sprite.pixelToRGB[xor ^ mask.getPixelFromBitStream(j, i)], (byte) (xor ^ mask.getPixelFromBitStream(j, i)));
            }
        }
        return decodedPlane;
    }
}
