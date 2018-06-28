package com.anlijiu.example.domain.objects;

public enum ItemType {
    ITEM_TYPE_WIDGET(1),
    ITEM_TYPE_SHORTCUT(2);

    private final int value;
    ItemType(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
