package com.example.fetch;

import java.util.Comparator;

public class ItemComparator implements Comparator<Item> {
    @Override
    public int compare(Item o1, Item o2) {
        if (o1.listId == o2.listId) {
            return o1.name.compareTo(o2.name);
        }
        return Integer.compare(o1.listId, o2.listId);
    }
}
