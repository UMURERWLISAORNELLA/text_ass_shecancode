package com.example.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Objects;

/**
 * Represents a key–value pair within the data management module.  This
 * class exposes JavaFX properties to integrate easily with table views
 * and other UI components.  Keys are unique identifiers used by
 * {@code DataManager} to store entries in a map.  Equality and
 * hashing are based solely on the key so that two entries with the
 * same key are considered equal regardless of their values.
 */
public class DataEntry {
    private final StringProperty key;
    private final StringProperty value;

    /**
     * Constructs a new data entry with the given key and value.
     *
     * @param key   the identifier for this entry; must not be null
     * @param value the associated value; may be null
     */
    public DataEntry(String key, String value) {
        this.key = new SimpleStringProperty(key);
        this.value = new SimpleStringProperty(value);
    }

    /** Returns the key for this entry. */
    public String getKey() {
        return key.get();
    }

    /** Sets the key for this entry. */
    public void setKey(String key) {
        this.key.set(key);
    }

    /** Exposes the key as a property for binding. */
    public StringProperty keyProperty() {
        return key;
    }

    /** Returns the value associated with this entry. */
    public String getValue() {
        return value.get();
    }

    /** Updates the value associated with this entry. */
    public void setValue(String value) {
        this.value.set(value);
    }

    /** Exposes the value as a property for binding. */
    public StringProperty valueProperty() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataEntry dataEntry = (DataEntry) o;
        return Objects.equals(getKey(), dataEntry.getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    @Override
    public String toString() {
        return "DataEntry{" + "key='" + getKey() + '\'' + ", value='" + getValue() + '\'' + '}';
    }
}