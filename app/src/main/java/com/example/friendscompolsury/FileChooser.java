package com.example.friendscompolsury;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.util.Arrays;

public class FileChooser {
    public static String mf_szGetRealPathFromURI(final Context context, final Uri ac_Uri )
    {
        String result = "";
        boolean isok = false;

        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(ac_Uri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
            isok = true;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return isok ? result : "";
    }
}
