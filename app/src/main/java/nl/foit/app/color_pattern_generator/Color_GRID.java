package nl.foit.app.color_pattern_generator;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Frits on 28-8-2015.
 */
public class Color_GRID extends AppCompatActivity {

    ArrayList<Color_part> mParts;
    Color_Collection adapter;
    SQLite db;
    Cursor cursor;
    String db1, db2, db3, db4, db5, date, name;
    GridView lvl;
    View empty;
    private Color_GRID cgrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_);
        lvl = (GridView) findViewById(R.id.gridView1);
        db = new SQLite(getApplicationContext());
        db.open();

        fileCheck();
        cursor = db.preferenceData();
        mParts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                date = cursor.getString(cursor.getColumnIndex(db.color_date));
                name = cursor.getString(cursor.getColumnIndex(db.color_name));
                String test = cursor.getString(cursor.getColumnIndex("_id"));

                String[] color = new String[29];
                int d = 1;
                for (int i = 0; i < color.length; i++) {
                    color[i] = cursor.getString(cursor.getColumnIndex("color_" + d++));
                }

                //name = cursor.getString(cursor.getColumnIndex(db.color_name));
                // date = cursor.getString(cursor.getColumnIndex(db.color_date));
                mParts.add(new Color_part(color, name, date, test));
                //mParts.add(new Color_part(num, ref, opm, dat, ned, date, name, test));
            } while (cursor.moveToNext());
        }

        adapter = new Color_Collection(getApplicationContext(), R.layout.single_row, mParts);

        adapter.notifyDataSetChanged();

        lvl.setAdapter(adapter);
        lvl.setEmptyView(findViewById(R.id.empty));
    }

    public void removeBy(int position, String id) {
        db.open();

        mParts.remove(position);
        adapter.notifyDataSetChanged();
        View lis = lvl.getChildAt(position);
        int top = (lis == null) ? 0 : lis.getTop();
        lvl.setSelection(top);
        db.remove_row(id);
        db.close();
        adapter.notifyDataSetChanged();
    }


    public void shareIntent(String foo) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        File a = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getPackageName() + "/temp_" + foo + ".png");
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(a));
        startActivity(Intent.createChooser(intent, getString(R.string.send)));


    }

    public void fileCheck() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName());
        file.mkdirs();
        if (file != null && file.exists()) {
            for (File image : file.listFiles()) {
                if (image.isFile() && image.getName().contains("temp_")) {
                    image.delete();
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_colors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(Color_GRID.this, NewColor.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
