package nl.foit.app.color_pattern_generator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Frits on 14-8-2015.
 */
public class Color_picker_adapter extends ArrayAdapter<Color_part> {

    private ArrayList<Color_part> objects;
    Color_part i;
    LayoutInflater inflater;
    private final Context context;

    public Color_picker_adapter(Context context, int resource, ArrayList<Color_part> objects) {
        super(context, resource, objects);
        this.objects = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.single_row, null);
            ViewHolder vHolder = new ViewHolder();
            vHolder.t1 = (TextView) v.findViewById(R.id.t1);
            vHolder.rl = (RelativeLayout) v.findViewById(R.id.newwave);
            //vHolder.locked = (ImageView) v.findViewById(R.id.locked);
            vHolder.checked = (RelativeLayout) v.findViewById(R.id.selected);
            vHolder.set = (ImageView) v.findViewById(R.id.edit_menu);
            v.setTag(vHolder);
        }

        i = objects.get(position);
        final ViewHolder viewholder = (ViewHolder) v.getTag();


        if (i.isChecked()) {
            viewholder.checked.setVisibility(View.VISIBLE);
        } else {
            viewholder.checked.setVisibility(View.INVISIBLE);
        }


        if (i != null) {
            if (i.getColor1() != null) {

                viewholder.t1.setText(i.getColor1());

                viewholder.rl.setBackgroundColor(Color.parseColor(i.getColor1()));
            }
        }

        viewholder.set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] foo = new String[objects.size()];
                String[] locked = new String[objects.size()];
                Intent intent = new Intent(getContext(), Dialog.class);
                intent.putExtra("position", position);
                intent.putExtra("color", viewholder.t1.getText().toString());
                intent.putExtra("size", ((NewColor) context).nBasic_list);


                for (int i = 0; i < objects.size(); i++) {
                    if (!objects.get(i).isChecked()) {
                        foo[i] = objects.get(i).getColor1();
                    } else {
                        locked[i] = objects.get(i).getColor1();
                    }
                }
                intent.putExtra("id", ((NewColor) context).id);

                intent.putExtra("getList", foo);
                intent.putExtra("getLocked", locked);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
            }
        });


        return v;
    }

    private class ViewHolder {
        TextView t1;
        RelativeLayout rl, checked;
        ImageView set;
    }
}