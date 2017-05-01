package com.qdota.dotashow;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.IOException;

/**
 * Created by zhaopan on 2016/8/11.
 */

public class TgaUtility {

    public static Bitmap getPreview(Context context) throws IOException {
        return Utility.TargaReader.decode(ResourceUtility.readAssets(context, "war3mapPreview.tga"));
    }
}
