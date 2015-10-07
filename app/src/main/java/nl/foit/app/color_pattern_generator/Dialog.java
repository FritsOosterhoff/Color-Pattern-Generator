package nl.foit.app.color_pattern_generator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Frits on 1-9-2015.
 */
public class Dialog extends Activity {
    Functions f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        View main_dial = getLayoutInflater().inflate(R.layout.dialog_la, null);
        final EditText color_tv = (EditText) main_dial.findViewById(R.id.color_string);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final Intent data = getIntent();
        final String color_data = data.getStringExtra("color");
        final int position = data.getIntExtra("position", 0);
        final String id = data.getStringExtra("id");
        if (color_data != null) {
            color_tv.setHint(color_data);
        }
        alertDialogBuilder.setView(main_dial);
        final String[] fromitnent = data.getStringArrayExtra("getList");

        f = new Functions();
        alertDialogBuilder.setPositiveButton(getString(R.string.positive), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String ent_color = color_tv.getText().toString();
                if (ent_color.length() > 0) {
                    if (ent_color.charAt(0) != '#') {
                        ent_color = "#" + color_tv.getText().toString();
                    }
                    if (ent_color.matches("^#([A-Fa-f0-9]{6})$")) {
                        //if (ent_color.matches("^#([A-Fa-f0-9]{6}||[A-Fóa-f0-9]{3})$")) { // 6 characters of 3 characters


                        Intent getBack = new Intent(Dialog.this, NewColor.class);

                        getBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getBack.putExtra("color", ent_color);
                        getBack.putExtra("position", position);
                        getBack.putExtra("getLocked", data.getStringArrayExtra("getLocked"));
                        getBack.putExtra("id", id);

                        if (fromitnent != null) {
                            getBack.putExtra("getList", fromitnent);
                        }
                        startActivity(getBack);

                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.hex_invalid), Toast.LENGTH_LONG).show();
                    }
                }
                finish();
            }
        }).setNegativeButton(getString(R.string.negative), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

//        ((Color_GRID) getApplicationContext()).fileCheck();

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        super.onCreate(savedInstanceState);
    }
}
