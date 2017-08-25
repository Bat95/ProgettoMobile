package com.myrecipebook.myrecipebook;

/**
 * Created by Thomas on 22/08/2017.
 */
import java.io.IOException;
import java.net.URL;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<String> values;
    List<Recipe> recipelist;
    Bitmap img;

    ResultAdapter(List<Recipe> recipelist){
        this.recipelist = recipelist;
    }

    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_ingredients, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String pic = recipelist.get(position).MainPic;
        try {
            URL url = new URL(pic);
            img = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch(IOException e) {
            System.out.println(e);
        }
        holder.image.setImageBitmap(img);
        holder.name.setText(recipelist.get(position).Name);
        holder.time.setText(recipelist.get(position).Duration);

        holder.layout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Codice per aprire la ricetta
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public ImageView image;
        public TextView name, time;
        public LinearLayout layout;

        public ViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.recipe_image);
            name = (TextView) v.findViewById(R.id.recipe_name);
            time = (TextView) v.findViewById(R.id.recipe_time);
            layout = (LinearLayout) v.findViewById(R.id.recipelayout);
        }

    }
    
}
