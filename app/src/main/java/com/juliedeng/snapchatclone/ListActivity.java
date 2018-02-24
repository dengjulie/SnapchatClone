package com.juliedeng.snapchatclone;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    final ArrayList<Snap> snaps = new ArrayList<>();
    public final ListAdapter adapter = new ListAdapter(this, snaps);
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference ref = firebaseDatabase.getReference("/snaps");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerAdapter = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerAdapter.setLayoutManager(new LinearLayoutManager(this));
        recyclerAdapter.setAdapter(adapter);


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snaps.clear();
                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    snaps.add(dataSnapshot2.getValue(Snap.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w("Database", "Failed to read value.", error.toException());
            }
        });
    }
}
