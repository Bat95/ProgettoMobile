package com.myrecipebook.myrecipebook;

/**
 * Created by Thomas on 22/08/2017.
 */
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private FragmentManager mFragmentManager;
    private List<String> values;
    List<Recipe> recipelist;
    Bitmap img;

    ResultAdapter(ArrayList<Recipe> recipelist, FragmentManager fm){
        this.recipelist = recipelist;
        this.mFragmentManager = fm;
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_recipe, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //image
        //String pic = recipelist.get(position).mainPic;
        //holder.image.setImageBitmap(img);

        new DownloadImageTask(holder.image).execute(recipelist.get(position).mainPic);

        //recipe name
        holder.n.setText(recipelist.get(position).name);

        //time
        int hours = recipelist.get(position).duration / 60;
        int minutes = recipelist.get(position).duration % 60;
        if (hours <= 0) holder.time.setText("Tempo: " + Integer.toString(minutes) + " minuti");
        else holder.time.setText("Tempo: " + Integer.toString(hours) + ":" + Integer.toString(minutes) + " ore");

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                img = ((BitmapDrawable)holder.image.getDrawable()).getBitmap();;
                Fragment f = new RecipeDetail(recipelist.get(position),img);
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.replace(R.id.content_main, f);
                ft.addToBackStack("results");
                ft.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipelist.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView image;
        public TextView n, time;
        public RelativeLayout layout;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.recipe_image);
            image.setImageResource(R.drawable.image_not_available);
            n = (TextView) v.findViewById(R.id.recipe_name);
            time = (TextView) v.findViewById(R.id.recipe_time);
            layout = (RelativeLayout) v.findViewById(R.id.recipelayout);
        }

    }
    
}
