package com.example.servimaq.db;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*public class Save {
    private Context TheThis;
    private String NameOfFolder = "/ImgServimaq";
    private String NameOfFile = "imagen_";

    public String SaveImage(Context context, Bitmap ImageToSave) throws IOException {

        TheThis = context;
        //String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder;
        //String file_path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString();
        String CurrentDateAndTime = getCurrentDateAndTime();

        //Log.e("safsa", "--------:: "+file_path);File dir = new File(file_path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        //File file = new File(dir, NameOfFile + CurrentDateAndTime + ".jpg");
        //File imagen = File.createTempFile(file,".jpg",file_path);


        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }
        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }
        return file.getAbsolutePath();
    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-­ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    private void UnableToSave() {
        Toast.makeText(TheThis, "¡No se ha podido guardar la imagen!", Toast.LENGTH_SHORT).show();
    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagen guardada en la galería.", Toast.LENGTH_SHORT).show();
    }
}*/
