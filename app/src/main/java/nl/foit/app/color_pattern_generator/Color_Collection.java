package nl.foit.app.color_pattern_generator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Frits on 14-8-2015.
 */
public class Color_Collection extends ArrayAdapter<Color_part> {

    private ArrayList<Color_part> objects;
    Color_part i;
    LayoutInflater inflater;
    private Context mContext;
    Intent intent;
    SQLite db;

    public Color_Collection(Context context, int resource, ArrayList<Color_part> objects) {
        super(context, resource, objects);
        this.objects = objects;
    }


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_row, null);
            ViewHolder vHolder = new ViewHolder();
            vHolder.t1 = (TextView) v.findViewById(R.id.t1);
            vHolder.mainlin = (LinearLayout) v.findViewById(R.id.main);
            vHolder.buton = (ImageView) v.findViewById(R.id.butn);
            vHolder.id = (TextView) v.findViewById(R.id.row_id);
            v.setTag(vHolder);
        }

        i = objects.get(position);
        final ViewHolder viewholder = (ViewHolder) v.getTag();

        if (viewholder.mainlin != null) {
            viewholder.mainlin.removeAllViews();
        }


        if (i != null) {


            if (viewholder.id != null) {
                if (i.getID() != null) {
                    viewholder.id.setText(i.getID());
                }
            }

            if (viewholder.t1 != null) {
                viewholder.t1.setText(i.getDate());
            }
            if (viewholder.mainlin != null) {
                if (i.getFoo() != null) {
                    for (String t : i.getFoo()) {
                        if (t != null) {
                            View nv = new View(getContext());
                            nv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, 2f));
                            nv.setBackgroundColor(Color.parseColor(t));
                            viewholder.mainlin.addView(nv);
                            viewholder.mainlin.setPadding(5, 5, 5, 5);
                        }
                    }
                }
            }
            db = new SQLite(getContext());
            if (viewholder.buton != null) {
                viewholder.buton.setColorFilter(R.color.abc_background_cache_hint_selector_material_dark);
                viewholder.buton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        PopupMenu pop = new PopupMenu(((Color_GRID) parent.getContext()).getSupportActionBar().getThemedContext(), viewholder.buton);
                        pop.getMenuInflater().inflate(R.menu.color_context, pop.getMenu());
                        final Color_part d = objects.get(position);
                        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.remove:
                                        ((Color_GRID) parent.getContext()).removeBy(position, d.getID());
                                        break;
                                    case R.id.edit:
                                        intent = new Intent(getContext(), NewColor.class);

                                        intent.putExtra("getLocked", d.getFoo());
                                        intent.putExtra("id", d.getID());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        getContext().startActivity(intent);

                                        //startActivity(Edit);
                                        break;
                                    case R.id.share:
                                        File f = null;
                                        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "/" + getContext().getPackageName());
                                        f.mkdirs();
                                        View wanted = viewholder.mainlin;


                                        OutputStream stream = null;
                                        wanted.setDrawingCacheEnabled(true);
                                        //wanted.layout(5, 5, wanted.getWidth(), wanted.getHeight());
//                                        wanted.layout(wanted.getWidth(), wanted.getHeight());
                                        wanted.layout(0, 0, wanted.getWidth(), wanted.getHeight());
                                        wanted.offsetTopAndBottom(15);
                                        wanted.offsetLeftAndRight(15);
                                        Bitmap t = Bitmap.createBitmap(wanted.getDrawingCache());
                                        String fa = String.valueOf(System.currentTimeMillis());
                                        try {
                                            stream = new FileOutputStream(f + "/temp_" + fa + ".png");
                                            t.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                            stream.flush();
                                            stream.close();

                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }

                                        ((Color_GRID) parent.getContext()).shareIntent(fa);


                                        break;
                                }
                                return false;
                            }
                        });
                        pop.show();
                    }
                });
            }
        }

        return v;
    }

    private class ViewHolder {
        TextView t1, id;
        LinearLayout mainlin;
        ImageView buton;
    }
}
