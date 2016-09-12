package edu.blooddonor.sqliteDB;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import edu.blooddonor.model.Station;

/**
 * Methods adding initial content to database.
 *
 * @author puszkarz
 *
 */
abstract class InitialContent {

    public static void stationsInit(SQLiteDatabase db) {

        ArrayList<Station> statList = new ArrayList<>();

        //Wrocław i Legnica
        statList.add(new Station("Regionalne Centrum Krwiodawstwa i Krwiolecznictwa im. prof. dr hab. Tadeusza Dorobisza",
                "ul. Czerwonego Krzyza 5, 50-345 Wroclaw"));
        statList.add(new Station("Oddział Terenowy Legnica, Wojewódzki Szpital Specjalistyczny",
                "ul. Iwaszkiewicza 5, 55-220 Legnica"));

        //Warszawa
        statList.add(new Station("Regionalne Centrum Krwiodawstwa i Krwiolecznictwa w Warszawie",
                "ul. Saska 63, 03-948 Warszawa"));
        statList.add(new Station("Oddział Terenowy Nr 12 RCKiK, Szpital Kliniczny Dzieciątka Jezus",
                "ul. Nowogrodzka 59, 02-005 Warszawa"));

        for (Station st : statList) {
            st.set_latLongAsNone();
            DatabaseHelper.insertStation(db, st);
        }
    }

}
