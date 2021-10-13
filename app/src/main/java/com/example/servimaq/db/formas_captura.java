package com.example.servimaq.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class formas_captura {

    private final String CARPETA_RAIZ="Servimaq/";
    File fileImagen;
    String nombreImagen = "";
    String fotoName="";
    String path;
    final int COD_FOTO=20;

    public void ImagenBase64(Bitmap bitmap){
        String fotoEnBase64 = null;

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        fotoEnBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //CONVERSION BASE64 a BITMAP
        byte[] decodedString = Base64.decode("base64", android.util.Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

    //FORMA CON PROVIDERR ******************************************
    /*private File crearImagen() throws IOException {
        String NombreImagen = "foto_";
        File directorio = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imagen = File.createTempFile(NombreImagen, ".jpg", directorio);
        ruta = imagen.getAbsolutePath();

        Log.e("ruta::","---: "+ruta);
        return imagen;
    }*/

    /*File imagenArchivo = null;

                try {
        imagenArchivo = crearImagen();
    } catch (IOException e) {
        e.printStackTrace();
    }

                if(imagenArchivo != null){
        Uri FotoUri = FileProvider.getUriForFile(getContext(),"com.example.servimaq.fileprovider",imagenArchivo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,FotoUri);
        startActivityForResult(intent,1);
    }*/ //FORMA CON PROVIDERR **********YOUTUBE********************************



    //CAPTURA CON PERMISOS******************************************************************
    /*public void checkPermissionStorage(){
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    GuardarImagen();
                }
            }
        }else{
            GuardarImagen();
        }
    }

    //CARGA DE IMAGEN--------------------------------------------------------------------------------------
    private void GuardarImagen(){
        OutputStream fos = null;
        File file = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContext().getContentResolver();
            ContentValues values = new ContentValues();
            String file_name = System.currentTimeMillis() + "imagen_";
            values.put(MediaStore.Images.Media.DISPLAY_NAME, file_name);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Servimaq");
            values.put(MediaStore.Images.Media.IS_PENDING,1);

            Uri colletion = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imageUri = resolver.insert(colletion,values);

            try {
                fos = resolver.openOutputStream(imageUri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values,null,null);
        }else{
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String file_name = System.currentTimeMillis() + ".jpg";
            file = new File(imageDir, file_name);

            try {
                fos = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        boolean guardar =  bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);

        if(guardar){
            Log.e("Mensaje", "___"+"Imagen guardada");
        }

        if(fos!=null){
            try {
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(file!=null){
            MediaScannerConnection.scanFile(getContext(),new String[]{file.toString()},null,null);
        }

    }*/
}
