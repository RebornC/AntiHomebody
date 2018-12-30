package com.example.yc.androidsrc.model;

/**
 * @author RebornC
 * Created on 2018/12/27.
 */

public class TodoListItem {
    private String text;

    public TodoListItem(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
