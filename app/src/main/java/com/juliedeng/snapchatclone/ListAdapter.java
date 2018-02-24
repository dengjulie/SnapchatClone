package com.juliedeng.snapchatclone;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.CustomViewHolder> {
    private Context context;
    private ArrayList<Snap> snaps;

    public ListAdapter(Context context, ArrayList<Snap> snaps) {
        this.context = context;
        this.snaps = snaps;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view, parent, false);
        return new CustomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final CustomViewHolder holder, int position) {
        Snap snap = snaps.get(snaps.size() - position - 1);
        holder.caption.setText(snap.getCaption());
        holder.email.setText(snap.getEmail());
        Glide.with(context).load(snap.getImageUrl()).into(holder.image);
        if (position %2 == 0) {
            holder.email.setTextColor(Color.parseColor("#32C7C7"));
            holder.caption.setTextColor(Color.parseColor("#32C7C7"));
            holder.card.setBackgroundColor(Color.parseColor("#ffffff"));

        }
    }

    @Override
    public int getItemCount() {
        return snaps.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView caption, email;
        ImageView image;
        ConstraintLayout card;

        public CustomViewHolder (View view) {
            super(view);
            this.caption = (TextView) view.findViewById(R.id.caption);
            this.email = (TextView) view.findViewById(R.id.email);
            this.image = (ImageView) view.findViewById(R.id.image);
            this.card = (ConstraintLayout) view.findViewById(R.id.card);
        }
    }
}
