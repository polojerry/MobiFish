package com.polotechnologies.mobifish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.polotechnologies.mobifish.R;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.List;

public class ProductFishRecyclerAdapter extends RecyclerView.Adapter<ProductFishRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<NewFish> newFishProducts;
    //Member Variable for OnClickListener
    final private FishItemClickListener mOnClickListener;

    public ProductFishRecyclerAdapter(Context mContext, List<NewFish> newFishProducts, FishItemClickListener fishItemClickListener) {
        this.mContext = mContext;
        this.newFishProducts = newFishProducts;
        this.mOnClickListener = fishItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_product_fish, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        NewFish newFish = newFishProducts.get(i);

        Glide.with(mContext)
                .load(newFish.getFishImageURl())
                .fitCenter()
                .into(viewHolder.mFishImage);
        viewHolder.mFishName.setText(newFish.getFishName());
        viewHolder.mFishPrice.setText(newFish.getFishPrice());
        //viewHolder.mFishDescription.setText(newFish.getFishDescriprion());

    }

    @Override
    public int getItemCount() {
        return newFishProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mFishName;
        TextView mFishPrice;
        TextView mFishDescription;
        ImageView mFishImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mFishName = itemView.findViewById(R.id.productFishName);
            mFishPrice = itemView.findViewById(R.id.productFishPrice);
            mFishImage = itemView.findViewById(R.id.productFish);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get the position of clicked item
            int clickedPosition = getAdapterPosition();

            NewFish mNewFish = newFishProducts.get(clickedPosition);

            String fishName = mNewFish.getFishName();
            String fishDescription = mNewFish.getFishDescriprion();
            String fishPrice = mNewFish.getFishPrice();
            String fishUrl = mNewFish.getFishImageURl();

            mOnClickListener.onFishItemClick(fishName, fishDescription, fishPrice,fishUrl);

        }
    }

    //Interface defining our listener
    public interface FishItemClickListener {
        /**
         * Method that takes in 5 parameters that is , fishName, fishDescription, fishPrice, fishImageUrl
         * to be passed to the new Activity
         * */
        void onFishItemClick(String fishName, String fishDescription, String fishPrice, String fishImageUrl);
    }
}
