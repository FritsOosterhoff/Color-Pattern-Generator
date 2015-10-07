package nl.foit.app.color_pattern_generator;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Frits on 16-8-2015.
 */
public class NewColor extends AppCompatActivity {
    public String nonc;
    ArrayList<Color_part> mParts;
    Color_picker_adapter adapter;
    ListView lv;
    Functions f;
    View Butn; //, name, array_color;
    Button genBut, savBut, plusBut;
    SQLite db;
    ContentValues cv;
    int nBasic_list;
    String foo;
    public String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        LayoutInflater inflater = getLayoutInflater();


        Butn = inflater.inflate(R.layout.butn, null); //Opties menu
        genBut = (Button) Butn.findViewById(R.id.generate); // het Generate Button
        savBut = (Button) Butn.findViewById(R.id.save); // het Save Button
        plusBut = (Button) Butn.findViewById(R.id.plus_one); // het Add One button

        lv = (ListView) findViewById(R.id.lv1);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.color_pick_title);
        lv.addFooterView(Butn);
        db = new SQLite(getApplicationContext());
        db.open();
        mParts = new ArrayList<>();
        adapter = new Color_picker_adapter(this, R.layout.single_row, mParts);
        f = new Functions();
        nBasic_list = 5;
        switchMethods(getIntent());

        id = getIntent().getStringExtra("id");
        adapter.notifyDataSetChanged();
        lv.setItemsCanFocus(false);
        lv.setAdapter(adapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {

                final Color_part color = adapter.getItem(position);
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


                if (getChecked(mParts)) { // kijkt of er tenminste 1 'checked' item aanwezig is.

                    for (Color_part cp : mParts) {  // Sorteert alle Child Elements van de ArrayList.
                        if (cp.isChecked()) {
                            cv.put("color_" + d++, cp.getColor1());
                        } else {
                            cp.setChecked(false);
                            cv.putNull("color_" + d);
                        }
                    }


                    cv.put(db.color_name, f.getDate());
                    cv.put(db.color_date, f.getDate());

                    if (id != null) {
                        db.getWritableDatabase().update(db.color_favorites_table, cv, db.id + "= " + id, null);
                    } else {
                        db.getWritableDatabase().insert(db.color_favorites_table, null, cv);
                    }
                    db.close();
                    Intent intent = new Intent(NewColor.this, Color_GRID.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "No colors are selected", Toast.LENGTH_LONG).show();
                }
            }
        });
        plusBut.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nBasic_list++;
                addRandom(nBasic_list, mParts);
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
                                          addRandom(nBasic_list, mParts);
                                          //setRemainingColors(mParts);

                                          adapter.notifyDataSetChanged();
                                          lv.setAdapter(adapter);
                                          lv.setSelectionFromTop(positin, top);
                                      }
                                  }

        );
    }

    public void switchMethods(Intent intent) {
        String[] locked = intent.getStringArrayExtra("getLocked");
        if (locked != null)
            setSaved(locked);

        String[] foo = intent.getStringArrayExtra("getList");
        if (foo != null)
            getIntentArray(foo);


        int pos = getIntent().getIntExtra("position", 0);
        String color_picked = intent.getStringExtra("color");
        if (color_picked != null) {
            if (adapter.getCount() != nBasic_list)
                nBasic_list = adapter.getCount();

            if (pos < nBasic_list)
                getEdits(color_picked, pos);

        }
        if (color_picked == null && locked == null && foo == null) {
            addRandom(nBasic_list, mParts);
        }
    }

    public void getEdits(String foo, int position) {
        if (foo != null) {
            mParts.remove(position);
            mParts.add(position, new Color_part(foo));
            mParts.get(position).setChecked(true);
            mParts.get(position).getID();
        }
    }

    public void getIntentArray(String[] foo) {
        for (String sav : foo) {
            if (sav != null) {
                mParts.add(new Color_part(sav));
            }
        }
    }

    public void setSaved(String[] foo) {
        getIntentArray(foo);
        for (Color_part cp : mParts) {
            cp.setChecked(true);
        }

    }

    public void addRandom(int size, ArrayList<Color_part> main) {
        while (main.size() < size)
            main.add(new Color_part(f.getHex()));

    }


    public void getSavedColors(ArrayList<Color_part> main, ArrayList<Color_part> locked) {
        for (Color_part color : locked) {
            color.setChecked(true);
            if (main.size() < nBasic_list) {
                main.add(color);
            }
        }
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
