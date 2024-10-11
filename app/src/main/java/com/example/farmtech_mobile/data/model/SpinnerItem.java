package com.example.farmtech_mobile.data.model;

public class SpinnerItem {
    private String text;
    private String arg1;
    private String arg2;

    public SpinnerItem(String text, String arg1, String arg2) {
        this.text = text;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public String getText() {
        return text;
    }

    public String getArg1() {
        return arg1;
    }
    public String getArg2() {
        return arg2;
    }

    @Override
    public String toString() {
        return text; // Isso vai definir o texto que será exibido no Spinner
    }
}
