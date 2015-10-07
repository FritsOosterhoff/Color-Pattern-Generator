package nl.foit.app.color_pattern_generator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Frits on 16-8-2015.
 */
public class Functions {

    public Functions() {

    }

    public String getHex() {
        StringBuilder stringBuilder = new StringBuilder();
        String SALTCHARS = "ABCDEF0123456789";
        Random rnd = new Random();
        while (stringBuilder.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            stringBuilder.append(SALTCHARS.charAt(index));
        }
        String finito = stringBuilder.toString();
        return "#" + finito;
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }


}
