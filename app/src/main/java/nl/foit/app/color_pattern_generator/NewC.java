package nl.foit.app.color_pattern_generator;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Frits on 16-8-2015.
 */
public class NewC extends AppCompatActivity {
    ArrayList<Color_part> mParts;
    Color_picker_adapter adapter;

    ListView lv;
    Functions f;
    View Butn, name, array_color;
    Button genBut, savBut, plusBut;
    SQLite db;
    ContentValues cv;
    EditText edit_name;
    String[] fromitnent;
    int nBasic_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        LayoutInflater inflater = getLayoutInflater();
        Butn = inflater.inflate(R.layout.butn, null);
        genBut = (Button) Butn.findViewById(R.id.generate);
        savBut = (Button) Butn.findViewById(R.id.save);
        plusBut = (Button) Butn.findViewById(R.id.plus_one);
        array_color = getLayoutInflater().inflate(R.layout.color_array, null);
        name = inflater.inflate(R.layout.name, null);
        edit_name = (EditText) name.findViewById(R.id.edit_name);
        lv = (ListView) findViewById(R.id.lv1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.color_pick_title);
        lv.addFooterView(Butn);
        lv.addFooterView(array_color);
        db = new SQLite(getApplicationContext());
        mParts = new ArrayList<>();
        adapter = new Color_picker_adapter(getApplicationContext(), R.layout.single_row, mParts);
        f = new Functions();
        Intent d = getIntent();
        fromitnent = d.getStringArrayExtra("getList");
        nBasic_list = 5;
        if (fromitnent == null) {
            while (adapter.getCount() < nBasic_list) {
                mParts.add(new Color_part(f.getHex()));
            }
        } else {
            for (String savedColor : fromitnent) {
                if (savedColor != null) {
                    mParts.add(new Color_part(savedColor));
                }
            }
            for (Color_part cp : mParts) {
                cp.setChecked(true);
            }
        }
        if (d.getStringExtra("color") != null) {

            final String color_picked = getIntent().getStringExtra("color");
            if (d.getIntExtra("position", 0) < 6) {
                int pos = d.getIntExtra("position", 0);
                mParts.remove(pos);
                mParts.add(pos, new Color_part(color_picked));
                mParts.get(pos).setChecked(true);
            }
        }
        adapter.notifyDataSetChanged();
        lv.setItemsCanFocus(false);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final Color_part color = adapter.getItem(position);
                lv.setItemChecked(position, true);
                color.toggleChecked();
                if (color.isChecked()) {
                    view.findViewById(R.id.selected).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.selected).setVisibility(View.INVISIBLE);
                }

            }
        });


        savBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  if (fromitnent == null) {

                cv = new ContentValues();
                int d = 1;

                if (getChecked(mParts)) {

                    for (Color_part cp : mParts) {
                        if (cp.isChecked()) {
                            cv.put("color_" + d++, cp.getColor1());
                        }
                    }

                    cv.put(db.color_name, f.getDate());
                    cv.put(db.color_date, f.getDate());
                    db.getWritableDatabase().insert(db.color_favorites_table, null, cv);
                    Intent intent = new Intent(NewC.this, Color_GRID.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No colors are selected", Toast.LENGTH_LONG).show();
                }
            }
        });
        plusBut.setOnClickListener(new View.OnClickListener() {
            int e = 0;

            @Override
            public void onClick(View v) {
                e++;
                AddOne(e, mParts);
                adapter.notifyDataSetChanged();
            }
        });
        genBut.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {

                                          ArrayList<Color_part> locked = new ArrayList<Color_part>();
                                          int positin = lv.getFirstVisiblePosition();
                                          View lis = lv.getChildAt(0);
                                          int top = (lis == null) ? 0 : lis.getTop();
                                          for (int d = 0; d < adapter.getCount(); d++) {
                                              Color_part de = adapter.getItem(d);

                                              if (de.isChecked()) {
                                                  locked.add(new Color_part(de.getColor1()));
                                              }
                                          }
                                          mParts.clear();

                                          getSavedColors(mParts, locked);
                                          setRemainingColors(mParts);
                                          adapter = new Color_picker_adapter(getApplicationContext(), R.layout.single_row, mParts);

                                          adapter.notifyDataSetChanged();
                                          lv.setAdapter(adapter);
                                          lv.setSelectionFromTop(positin, top);
                                      }
                                  }

        );
    }

    public void setQuery(SQLite db, ArrayList<Color_part> main) {
        ContentValues cv = new ContentValues();
        cv.put(db.color_name, f.getDate());
        cv.put(db.color_date, f.getDate());
        int d = 1;
        for (Color_part cp : main) {
            if (cp.isChecked()) {
                cv.put("color_" + d++, cp.getColor1());
            }
        }
    }

    public void AddOne(int size, ArrayList<Color_part> main) {
        while (main.size() < 5 + size)
            main.add(new Color_part(f.getHex()));
    }

    public void getBooted(int position, View v) {
        String[] foo = new String[mParts.size()];
        Intent intent = new Intent(getApplicationContext(), Dialog.class);
        intent.putExtra("position", position);
        TextView t = (TextView) v.findViewById(R.id.t1);
        String f = t.getText().toString();
        intent.putExtra("color", f);
        int d = 0;
        for (Color_part c : mParts) {
            foo[d++] = c.getColor1();
        }
        intent.putExtra("getList", foo);
        startActivity(intent);
    }

    public void setRemainingColors(ArrayList<Color_part> main) {
        while (main.size() < 5)
            main.add(new Color_part(f.getHex()));
    }

    public void getSavedColors(ArrayList<Color_part> main, ArrayList<Color_part> locked) {
        for (Color_part color : locked) {
            color.setChecked(true);
            if (main.size() < 5) {
                main.add(color);
            }
        }
    }

    public void getColors(String[] shared_colors, ArrayList<Color_part> main) {
        if (shared_colors != null) {
            for (String fo : shared_colors) {
                if (main.size() < 5) {
                    main.add(new Color_part(fo));
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.edit:

                final AlertDialog.Builder adb = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));
                adb.setTitle(R.string.color_edit_title).setMessage(R.string.color_edit_message);

                LinearLayout d;
                d = new LinearLayout(getApplicationContext());
                for (Color_part cp : mParts) {

                    EditText input = new EditText(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT, 400);
                    input.setLayoutParams(lp);
                    input.setHint(cp.getColor1());
                    d.addView(input);
                }


                adb.setView(d);

                adb.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
                break;
            default:
                break;
        }

        return super.

                onOptionsItemSelected(item);

    }


    public boolean getChecked(ArrayList<Color_part> test) {
        int i = 0;
        for (Color_part cp : test) {
            if (cp.isChecked()) {
                i++;
            }
        }

        if (i > 0) {
            return true;
        } else {
            return false;
        }
    }


}
