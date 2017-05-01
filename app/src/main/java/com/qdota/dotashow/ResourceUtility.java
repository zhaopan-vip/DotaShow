package com.qdota.dotashow;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhaopan on 2016/6/16.
 */

public class ResourceUtility {
    public static byte[] getStringByInputStream(InputStream is) {
        try {
            if (is != null) {
                byte[] buffer = new byte[is.available()];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1)
                        break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                return buffer;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return null;
    }

    public static byte[] readAssets(Context context, String fileName) {
        InputStream is = null;
        try {
            is = context.getAssets().open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getStringByInputStream(is);
    }

    public static byte[] readRaws(Context context, int id) {
        InputStream is = context.getResources().openRawResource(id);
        return getStringByInputStream(is);
    }
}
