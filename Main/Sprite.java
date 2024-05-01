import java.awt.image.BufferedImage;

public class Sprite {
    public static int[] pixelToRGB = new int[]{0xffffffff, 0xffaaaaaa, 0xff555555, 0xff000000};
    public BufferedImage image;
    public StringBuilder bitStream = new StringBuilder();
    public int width;
    public int height;
    public boolean isPlane;

    public Sprite(BufferedImage image, boolean isPlane) {
        this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.isPlane = isPlane;
        for(int i = 0; i < height ; i++) {
            for (int j = 0; j < width; j++) {
                final byte clr = (byte) ((image.getRGB(j, i) & 3) ^ 3);
                bitStream.append(String.format("%2s", Integer.toBinaryString(clr)).replace(' ', '0'));
            }
        }
        this.updateFromBitStream(isPlane);
    }

    public Sprite(int width, int height, boolean isPlane) {
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < width ; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, 0x00000000);
            }
        }
        this.width = width;
        this.height = height;
        this.isPlane = isPlane;
        bitStream.append("0".repeat(width * height * (this.isPlane ? 1 : 2)));
    }
    public Sprite(Sprite[] planes) {
        this.width = planes[0].image.getWidth();
        this.height = planes[0].image.getHeight();
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.isPlane = false;
        if (planes[0].bitStream.length() + planes[1].bitStream.length() <= width * height * 2) {
            this.bitStream = new StringBuilder(planes[0].bitStream.toString() + planes[1].bitStream);
        } else {
            this.bitStream = new StringBuilder(planes[0].bitStream + "0".repeat(width * height * 2 - planes[0].bitStream.length()) + planes[1].bitStream);
        }

        this.updateFromBitStream(false);
    }

    public Sprite[] splitPlanes() {
        int height = this.height;
        int width = this.width;
        Sprite MSB = new Sprite(width, height, true);
        Sprite LSB = new Sprite(width, height, true);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                byte pixel = this.getPixelFromBitStream(j, i);
                MSB.updatePlane(j, i, pixelToRGB[pixel & 2 | (pixel >> 1)], (byte) (pixel >> 1));
                LSB.updatePlane(j, i, pixelToRGB[(pixel << 1 | (pixel & 1)) & 3], (byte) (pixel & 1));
            }
        }
        MSB.updateFromBitStream(true);
        LSB.updateFromBitStream(true);
        return new Sprite[]{MSB, LSB};
    }

    public static Sprite mergePlanes(Sprite[] planes) {
        int width = planes[0].image.getWidth();
        int height = planes[0].image.getHeight();
        Sprite mergedSprite = new Sprite(width, height, false);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                byte msb = (byte) (planes[0].getPixelFromBitStream(j, i) << 1);
                byte lsb = planes[1].getPixelFromBitStream(j, i);
                int mergedPixel = msb + lsb;
                mergedSprite.update(j, i, pixelToRGB[mergedPixel], (byte) mergedPixel);
            }
        }
        mergedSprite.updateFromBitStream(false);
        return mergedSprite;
    }

    public void update(int x, int y, int argb, byte value) {
        int loc = y*image.getWidth()+x;
        image.setRGB(x, y, argb);
        bitStream.replace(loc*2, loc*2+2, String.format("%2s", Integer.toBinaryString(value)).replace(' ', '0'));
    }
    public void updatePlane(int x, int y, int argb, byte value) {
        int loc = y*image.getWidth()+x;
        image.setRGB(x, y, argb);
        bitStream.replace(loc, loc+1, String.format("%1s", Integer.toBinaryString(value)).replace(' ', '0'));
    }
    public void updateFromBitStream(boolean isPlane) {
        // isPlane checks to see if the input is at 1 bit per pixel or 2 bits per pixel
        if(!isPlane) {
            for (int i = 0; i < bitStream.length(); i += 2) {
                byte current = Byte.valueOf(bitStream.substring(i, i + 2), 2);
                int x = (i / 2) % image.getWidth();
                int y = (i / 2) / image.getWidth();
                if(Main.verbose >=6 ) System.out.println("x: " + x + ", y: " + y + ", current: " + current);
                image.setRGB(x, y, pixelToRGB[current]);
            }
        }
        else{
            if (this.bitStream.length() % 2 != 0) this.bitStream.append("0");
            for (int i = 0; i < bitStream.length(); i++) {
                byte current = Byte.valueOf(bitStream.substring(i, i + 1), 2);
                int x = i % image.getWidth();
                int y = i / image.getWidth();
                image.setRGB(x, y, pixelToRGB[current<<1 | current]);
            }
        }
    }

    public byte getPixelFromBitStream(int x, int y) {
        try {
            return extractFromBitStream((y * image.getWidth() + x) * (isPlane ? 1 : 2), (isPlane ? 1 : 2));
        }
        catch (StringIndexOutOfBoundsException e){
            return 0;
        }
    }

    protected byte extractFromBitStream(int start, int end) {
        return Byte.valueOf(bitStream.substring(start, start + end), 2);
    }
}
