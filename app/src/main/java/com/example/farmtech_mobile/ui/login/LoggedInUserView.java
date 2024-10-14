package com.example.farmtech_mobile.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    private String displayId;


    LoggedInUserView(String displayId,String displayName) {
        this.displayId = displayId;
        this.displayName = displayName;
    }
    public String getDisplayId() {
        return displayId;
    }

    public void setDisplayID(String displayId) {
        this.displayId = displayId;
    }

    String getDisplayName() {
        return displayName;
    }
}