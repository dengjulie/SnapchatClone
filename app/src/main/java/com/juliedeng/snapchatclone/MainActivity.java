package com.juliedeng.snapchatclone;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int PICTURE_UPLOAD = 1;
    EditText caption;
    Button submit_button;
    ImageButton list_button;
    ImageView image;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("/snaps");
    private StorageReference storageRef;

    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;

    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        caption = findViewById(R.id.caption);
        image = findViewById(R.id.image);
        submit_button = findViewById(R.id.submit_button);
        list_button = findViewById(R.id.list_button);
        View.OnFocusChangeListener keyboardHider = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICTURE_UPLOAD);
            }
        });

        list_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
        caption.setOnFocusChangeListener(keyboardHider);

        caption.setVisibility(View.GONE);
        submit_button.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Log.d("AddSocialActivity", "????");
        }

        if (requestCode == PICTURE_UPLOAD && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                String path = selectedImageUri.getPath();
                Log.e("image path", path + "");
                image.setImageURI(selectedImageUri);
            }
            caption.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.VISIBLE);
        }
    }

    public void submit() {
        ref = FirebaseDatabase.getInstance().getReference();

        final String key = ref.child("snaps").push().getKey();
        storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("https://snapchatclone-f21ed.firebaseio.com/");
        StorageReference socialsRef = storageRef.child(key + ".png");

        if (selectedImageUri == null) {
            Log.d("SUBMIT", "image null");
            return;
        }
        socialsRef.putFile(selectedImageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Cannot upload file into storage.", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String _caption = caption.getText().toString();
                String email  = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String imageURL = taskSnapshot.getDownloadUrl().toString();


                Snap snap = new Snap(imageURL, _caption, email, key);
                ref.child("snaps").child(key).setValue(snap);
                startActivity(new Intent(MainActivity.this, ListActivity.class));
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
