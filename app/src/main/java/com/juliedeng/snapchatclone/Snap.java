package com.juliedeng.snapchatclone;

import java.io.Serializable;

/**
 * Created by juliedeng on 2/23/18.
 */

public class Snap implements Serializable {
    String imageUrl, caption, firebaseKey, email;

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
