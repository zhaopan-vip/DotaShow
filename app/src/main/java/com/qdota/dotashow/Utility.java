package com.qdota.dotashow;

import android.graphics.Bitmap;

import java.io.IOException;

import static android.R.string.copy;

/**
 * Created by zhaopan on 2016/6/16.
 */
public class Utility {

    public static class TargaReader {

        private static int offset;

        private static int btoi(byte b) {
            int a = b;
            return (a < 0 ? 256 + a : a);
        }

        private static int read(byte[] buf) {
            return btoi(buf[offset++]);
        }

        public static Bitmap decode(byte[] buf) throws IOException {
            offset = 0;

            // Reading header bytes
            // buf[2]=image type code 0x02=uncompressed BGR or BGRA
            // buf[12]+[13]=width
            // buf[14]+[15]=height
            // buf[16]=image pixel size 0x20=32bit, 0x18=24bit
            // buf{17]=Image Descriptor Byte=0x28 (00101000)=32bit/origin
            //         upperleft/non-interleaved
            for (int i = 0; i < 12; i++)
                read(buf);
            int width = read(buf) + (read(buf) << 8);   // 00,04=1024
            int height = read(buf) + (read(buf) << 8);  // 40,02=576
            read(buf);
            int desc = read(buf);
            int orientation = (desc >> 4) & 0x3;
            int alpha = desc & 0xf;

            int n = width * height;
            int[] pixels = new int[n];
            int idx = 0;

            if (buf[2] == 0x02 && buf[16] == 0x20) { // uncompressed BGRA
                while (n > 0) {
                    int b = read(buf);
                    int g = read(buf);
                    int r = read(buf);
                    int a = read(buf);
                    int v = (a << 24) | (r << 16) | (g << 8) | b;
                    pixels[idx++] = v;
                    n -= 1;
                }
            } else if (buf[2] == 0x02 && buf[16] == 0x18) {  // uncompressed BGR
                while (n > 0) {
                    int b = read(buf);
                    int g = read(buf);
                    int r = read(buf);
                    int a = 255; // opaque pixel
                    int v = (a << 24) | (r << 16) | (g << 8) | b;
                    pixels[idx++] = v;
                    n -= 1;
                }
            } else {
                // RLE compressed
                while (n > 0) {
                    int nb = read(buf); // num of pixels
                    if ((nb & 0x80) == 0) { // 0x80=dec 128, bits 10000000
                        for (int i = 0; i <= nb; i++) {
                            int b = read(buf);
                            int g = read(buf);
                            int r = read(buf);
                            pixels[idx++] = 0xff000000 | (r << 16) | (g << 8) | b;
                        }
                    } else {
                        nb &= 0x7f;
                        int b = read(buf);
                        int g = read(buf);
                        int r = read(buf);
                        int v = 0xff000000 | (r << 16) | (g << 8) | b;
                        for (int i = 0; i <= nb; i++)
                            pixels[idx++] = v;
                    }
                    n -= nb + 1;
                }
            }

            Bitmap bimg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bimg.setPixels(pixels, 0, width, 0, 0, width, height);

            switch (orientation) {
                case 0x2:
                    return bimg;
                case 0x0:
                    return yReversal(width, height, pixels);
            }
            return bimg;
        }
    }

    public static Bitmap yReversal(final int width, final int height, int[] piexels) {
        int n = width * height;
        int[] buf = new int[n];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                buf[y * width + x] = piexels[(height - 1 - y) * width + x];
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(buf, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
