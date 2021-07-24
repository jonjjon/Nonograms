package edu.skku.map.pa2t1;

import org.json.JSONArray;
import org.json.JSONObject;

public class DataModel {
    public String lastBuildDate;
    public int total;
    public int start;
    public int display;
    public Item[] items;

    class Item {
        public String title;
        public String link;
        public String thumbnail;
        public String sizeheight;
        public String sizewidth;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = lastBuildDate;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getDisplay() {
        return display;
    }

    public void setDisplay(int display) {
        this.display = display;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
}
