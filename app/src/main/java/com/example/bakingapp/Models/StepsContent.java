package com.example.bakingapp.Models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample sDescription for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class StepsContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<StepItem> ITEMS = new ArrayList<StepItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, StepItem> ITEM_MAP = new HashMap<String, StepItem>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(StepItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.sId, item);
    }

    private static StepItem createDummyItem(int position) {
        return new StepItem(String.valueOf(position), "Item " + position, makeDetails(position));
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore sVideoUrl information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of sDescription.
     */
    public static class StepItem {
        public final String sId;
        public final String sDescription;
        public final String sVideoUrl;


        public StepItem(String sId, String sDescription, String sVideoUrl) {
            this.sId = sId;
            this.sDescription = sDescription;
            this.sVideoUrl = sVideoUrl;
        }

        @Override
        public String toString() {
            return sDescription;
        }
    }
}
