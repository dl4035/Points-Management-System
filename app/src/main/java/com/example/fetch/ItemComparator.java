package com.example.fetch;

import java.util.Comparator;

/**
 * Custom comparator class for the Item objects.
 * It first compares items based on their 'listId' and then based on their 'name'.
 */
public class ItemComparator implements Comparator<Item> {

    /**
     * Compares two items primarily by their 'listId' and secondarily by their 'name'.
     *
     * @param o1 First item to be compared.
     * @param o2 Second item to be compared.
     * @return A negative integer, zero, or a positive integer as the first item is less than,
     *         equal to, or greater than the second item respectively.
     */
    @Override
    public int compare(Item o1, Item o2) {
        // If the listIds are the same, compare by the name of the item.
        if (o1.listId == o2.listId) {
            return o1.name.compareTo(o2.name);
        }
        // Otherwise, compare by listId.
        return Integer.compare(o1.listId, o2.listId);
    }
}
