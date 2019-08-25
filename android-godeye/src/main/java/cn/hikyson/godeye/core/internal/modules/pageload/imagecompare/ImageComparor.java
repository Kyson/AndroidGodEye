package cn.hikyson.godeye.core.internal.modules.pageload.imagecompare;

import android.graphics.Bitmap;


/**
 * https://rosettacode.org/wiki/Percentage_difference_between_images#Java
 * http://www.ruanyifeng.com/blog/2013/03/similar_image_search_part_ii.html
 * https://gist.github.com/kuFEAR/6e20342198d4040e0bb5
 * https://www.cnblogs.com/faith0217/articles/4088386.html
 */
public class ImageComparor {

    private static double getDifferencePercent(Bitmap img1, Bitmap img2) {
        BaseRenderScriptComparer
        int width = img1.getWidth();
        int height = img1.getHeight();
        int width2 = img2.getWidth();
        int height2 = img2.getHeight();
        long diff = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                diff += pixelDiff(img1.get(x, y), img2.getRGB(x, y));
            }
        }
        long maxDiff = 3L * 255 * width * height;

        return 100.0 * diff / maxDiff;
    }

    private static int pixelDiff(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;
        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;
        return Math.abs(r1 - r2) + Math.abs(g1 - g2) + Math.abs(b1 - b2);
    }
}
