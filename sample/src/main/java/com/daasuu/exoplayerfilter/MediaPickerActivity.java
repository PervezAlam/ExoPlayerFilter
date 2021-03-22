package com.daasuu.exoplayerfilter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.exoplayer2.util.Log;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

/**
 * author : abhirath@adobe.com
 * date : March 03, 2021
 */

public class MediaPickerActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_EXTERNAL_STORAGE_ON_RESUME = 1010;

    Button mediaPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_picker);
        mediaPicker = findViewById(R.id.media_picker_);
        mediaPicker.setOnClickListener(v->startPicker());
    }

    @Override
    public void onResume() {
        Log.d("ABHI-Media_Picker", "onResume");

        super.onResume();
        if (!verifyStoragePermissions(this)) {
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE_ON_RESUME);
        }

    }

    public boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions,grantResults);
        if (requestCode == REQUEST_EXTERNAL_STORAGE_ON_RESUME) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("ABHI", " Permission Granted , called from on Resume");
            } else {
                android.util.Log.d("ABHISHEK", "Permission not given called from on Resume");
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==123){
            if (resultCode == Activity.RESULT_OK) {
                Uri selectedVideoUri = data.getData();
                String selectedVideoPath = getRealPathFromURI(selectedVideoUri);
                launchPlayer(selectedVideoPath);
                //DEFAULT_MEDIA_URI = Uri.parse(selectedVideoPath);
                Log.d("ABHISHEK", " Selected Media Uri = " + selectedVideoUri);
                Log.d("ABHISHEK", " Selected Path = " + selectedVideoPath);
            } else {
                Toast.makeText(this, " No Video Selected", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void launchPlayer(String mediaPath) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setData(Uri.parse(mediaPath));
        startActivity(intent);
    }

    public String getRealPathFromURI(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }

    private void startPicker(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);
    }

    public void launchImageSelection(){
        Intent intent = new Intent (Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        intent.setType("video/*");
        startActivityForResult(Intent.createChooser(intent,"Select Video"),112);
    }

    public static String GetRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}