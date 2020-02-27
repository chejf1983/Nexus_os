/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nexus.main.compent;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 * @author Administrator
 */
public abstract class ImageHelper {

    public static BufferedImage mergeImage(BufferedImage img1,
            BufferedImage img2, boolean isHorizontal) throws IOException {
        int w1 = img1.getWidth();
        int h1 = img1.getHeight();
        int w2 = img2.getWidth();
        int h2 = img2.getHeight();

        // 从图片中读取RGB
        int[] ImageArrayOne = new int[w1 * h1];
        ImageArrayOne = img1.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
        int[] ImageArrayTwo = new int[w2 * h2];
        ImageArrayTwo = img2.getRGB(0, 0, w2, h2, ImageArrayTwo, 0, w2);

        // 生成新图片
        BufferedImage DestImage = null;
        if (isHorizontal) { // 水平方向合并
            DestImage = new BufferedImage(w1 + w2, h1, BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(w1, 0, w2, h2, ImageArrayTwo, 0, w2);
        } else { // 垂直方向合并
            DestImage = new BufferedImage(w1, h1 + h2,
                    BufferedImage.TYPE_INT_RGB);
            DestImage.setRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
            DestImage.setRGB(0, h1, w2, h2, ImageArrayTwo, 0, w2); // 设置下半部分的RGB
        }

        return DestImage;
    }
}
