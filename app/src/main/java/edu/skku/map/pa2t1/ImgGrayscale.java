package edu.skku.map.pa2t1;

import android.graphics.Bitmap;
import android.graphics.Color;

public class ImgGrayscale {
    Bitmap bmpImg;


    public ImgGrayscale(Bitmap img){
        Bitmap imgResized = changeSize(img);
        this.bmpImg = changeIntoGrayscale(imgResized);
    }


    private Bitmap changeSize(Bitmap bmpOriginal){
        Bitmap bmpResized;
        bmpResized = Bitmap.createScaledBitmap(bmpOriginal, 360, 360, true);
        return bmpResized;
    }


    private Bitmap changeIntoGrayscale(Bitmap bmpOriginal){
        int width, height;
        width = bmpOriginal.getWidth();
        height = bmpOriginal.getHeight();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);

        int Alpha, Red, Green, Blue;
        int pixel;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {

                pixel = bmpOriginal.getPixel(x, y);
                Alpha = Color.alpha(pixel);
                Red = Color.red(pixel);
                Green = Color.green(pixel);
                Blue = Color.blue(pixel);
                int pixelGrayscale = (int) (0.2989 * Red + 0.5870 * Green + 0.1140 * Blue);

                if (pixelGrayscale > 128)
                    pixelGrayscale = 255;
                else
                    pixelGrayscale = 0;
                bmpGrayscale.setPixel(x, y, Color.argb(Alpha, pixelGrayscale, pixelGrayscale, pixelGrayscale));
            }
        }
        return bmpGrayscale;
    }
}
