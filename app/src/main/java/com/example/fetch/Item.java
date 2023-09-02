package com.example.fetch;

/**
 * Represents an individual item retrieved from the Fetch API.
 * Each item has three properties: a listId, an id, and a name.
 */
public class Item {

    /**
     * Represents the identifier for the group to which the item belongs.
     * Items with the same listId are considered to be part of the same group.
     */
    int listId;

    /**
     * Represents the unique identifier for the item.
     * Each item in the API has a distinct id.
     */
    int id;

    /**
     * Represents the name of the item. It can be any string value.
     */
    String name;
}
