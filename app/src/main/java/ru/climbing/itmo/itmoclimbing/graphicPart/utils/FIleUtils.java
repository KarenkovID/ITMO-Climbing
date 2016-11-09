package ru.climbing.itmo.itmoclimbing.graphicPart.utils;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Игорь on 17.10.2016.
 */

public class FIleUtils {
    public static String readTextFromRaw(Context context, int resourceID) {
        StringBuilder stringBuilder = new StringBuilder();
        try (
                InputStream inputStream =
                        context.getResources().openRawResource(resourceID);
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(inputStream))
                ){
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\r\n");
            }
        } catch (IOException | Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
