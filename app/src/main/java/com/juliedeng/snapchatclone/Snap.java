package com.juliedeng.snapchatclone;

import java.io.Serializable;

public class Snap implements Serializable {
    String imageUrl, caption, firebaseKey, email;

    public Snap() {
        this.imageUrl = null;
        this.caption = null;
        this.firebaseKey = null;
        this.email = null;
    }

    public Snap(String imageUrl, String caption, String firebaseKey, String email) {
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.firebaseKey = firebaseKey;
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public String getEmail() {
        return email;
    }
}
