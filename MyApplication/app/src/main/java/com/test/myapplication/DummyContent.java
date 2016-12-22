package com.test.myapplication;

import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;


import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID.*;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of (dummy) items.
     */
    public static List<DummyItem> ITEMS = new ArrayList<DummyItem>();

    /**
     * A map of (dummy) items, by ID.
     */
    public static Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    static {
        // Add 3 sample items.
        addItem(new DummyItem("1", "12/4/2016 4:00pm", "Can discarded on the way side", "http://i68.tinypic.com/2zf52l3.jpg","37.336876"," -121.881908","79 South 7th Street","Minor","Small","still_there"));

       // addItem(new DummyItem("2", "12/3/2016 6:00pm", "Trash on the way side", "http://i63.tinypic.com/20gwsy.jpg","37.336534","-121.884468","220-224 East San Fernando Street","Medium","Medium","removal_claimed"));
       // addItem(new DummyItem("3", "12/2/2016 7:00pm", "Bags on the way side", "http://i67.tinypic.com/9us01u.png","37.334917","-121.885203","100-198 South 4th Street","Severe","Large","removal_claimed"));
    }

    private static void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public String id;
        public String content;
        public String meaning;
        public String imageurl;
        public String lat;
        public String longt;
        public String address;
        public String Severity;
        public String Size;
        public String Status;

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        public DummyItem(String id, String content, String meaning, String imageurl, String lat, String longt, String address,String Severity,String Size, String Status) {
            this.id = id;
            this.content = content;
            this.meaning = meaning;
            this.imageurl= imageurl;
            this.lat= lat;
            this.longt= longt;
            this.address= address;
            this.Severity = Severity;
            this.Size = Size;
            this.Status = Status;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}