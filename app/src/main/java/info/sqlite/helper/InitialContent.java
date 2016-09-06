package info.sqlite.helper;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import info.sqlite.model.Station;

/**
 * Methods adding initial content to database.
 */
abstract class InitialContent {

    private static final String LOG_TAG = "InitContent: ";

    public static void stationsInit(SQLiteDatabase db) {
        ArrayList<Station> statList = new ArrayList<>();

        //Wrocław i Legnica
        statList.add(new Station("Regionalne Centrum Krwiodawstwa i Krwiolecznictwa im. prof. dr hab. Tadeusza Dorobisza",
                "ul. Czerwonego Krzyża 5/9, 50-345 Wrocław"));
        statList.add(new Station("Oddział Terenowy Legnica, Wojewódzki Szpital Specjalistyczny",
                "ul. Iwaszkiewicza 5, 55-220 Legnica"));

        //Warszawa
        statList.add(new Station("Regionalne Centrum Krwiodawstwa i Krwiolecznictwa w Warszawie",
                "ul. Saska 63/75, 03-948 Warszawa"));
        statList.add(new Station("Oddział Terenowy Nr 12 RCKiK, Szpital Kliniczny Dzieciątka Jezus",
                "ul. Nowogrodzka 59, 02-005 Warszawa"));


        for (Station st : statList) {
            st.set_latitude(0.0);
            st.set_longitude(0.0);
        }

        for (Station st : statList) {
            if (st.isWellDefined()) {
                DatabaseHelper.insertStation(db, st);
            } else {
                Log.e(LOG_TAG, "Station not well defined. Not added to db. Stat: " + st.toString());
            }
        }
    }
}
