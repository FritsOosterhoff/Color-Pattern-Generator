package nl.foit.app.color_pattern_generator;

/**
 * Created by Frits on 14-8-2015.
 */


public class Color_part {

    private String color1, color6, color7, _id;
    private boolean checked = false;

    private String[] foo;


    public Color_part() {
    }


    public Color_part(String[] foo, String name, String date, String id) {
        this.foo = foo;
        this.color6 = name;
        this.color7 = date;
        this._id = id;
    }

    public Color_part(String first) {
        this.color1 = first;
    }

    public Color_part(String first, String id) {
        this.color1 = first;
        this._id = id;
    }

    public String getDate() {
        return color6;
    }

    public String getName() {
        return color7;
    }


    public String getID() {
        return _id;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }


    public String[] getFoo() {
        return foo;
    }


    public String getColor1() {
        return color1;
    }

    public boolean isChecked() {
        return checked;
    }


    public void toggleChecked() {
        checked = !checked;
    }

}
