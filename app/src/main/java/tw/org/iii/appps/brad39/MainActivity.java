package tw.org.iii.appps.brad39;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private AudioManager amgr;
    private File sdroot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        sdroot = Environment.getExternalStorageDirectory();

    }

    public void test1(View view) {
        amgr.playSoundEffect(AudioManager.FX_KEYPRESS_INVALID,1);
    }
    public void test2(View view) {
        amgr.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD,1);
    }
    public void test3(View view) {
        amgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_RAISE,0);
    }
    public void test4(View view) {
        amgr.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                AudioManager.ADJUST_LOWER,0);
    }

    public void test5(View view) {

        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(sdroot, "brad20190825.amr")));

        startActivityForResult(intent, 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Log.v("brad", "OK");
            // Sound recorder does not support EXTRA_OUTPUT
            Uri uri = data.getData();
            try {
                String filePath = getAudioFilePathFromUri(uri);
                Log.v("brad", filePath);
//                copyFile(filePath, new File(sdroot, "brad.amr"));
//                getContentResolver().delete(uri, null, null);
//                (new File(filePath)).delete();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }else if (resultCode == RESULT_CANCELED){
            Log.v("brad", "Cancel");
        }
    }

    private String getAudioFilePathFromUri(Uri uri) {
        Cursor cursor = getContentResolver()
                .query(uri, null, null, null, null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA);
        return cursor.getString(index);
    }

    private void copyFile(String fileName, File target) throws IOException {
        Files.copy(new File(fileName), target);
    }

}
