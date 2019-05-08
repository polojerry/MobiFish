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
import com.polotechnologies.mobifish.dataModels.Cart;
import com.polotechnologies.mobifish.dataModels.NewFish;

import java.util.List;

public class CartFishRecyclerAdapter extends RecyclerView.Adapter<CartFishRecyclerAdapter.ViewHolder> {

    Context mContext;
    List<Cart> newFishCart;
    //Member Variable for OnClickListener
    final private CartItemClickListener mOnClickListener;

    public CartFishRecyclerAdapter(Context mContext, List<Cart> newFishCart, CartItemClickListener mOnClickListener) {
        this.mContext = mContext;
        this.newFishCart = newFishCart;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.card_fish_cart, viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Cart newCart = newFishCart.get(i);

        Glide.with(mContext)
                .load(newCart.getFishImageUrl())
                .fitCenter()
                .into(viewHolder.mFishImage);
        viewHolder.mFishName.setText(newCart.getFishName());
        viewHolder.mFishPrice.setText(String.format("Price Each: %s", newCart.getFishpriceEach()));
        viewHolder.mFishTotalPrice.setText(String.format("Total Price: %s", newCart.getFishTotalPrice()));

    }

    @Override
    public int getItemCount() {
        return newFishCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView mFishName;
        TextView mFishPrice;
        TextView mFishTotalPrice;
        ImageView mFishImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mFishName = itemView.findViewById(R.id.productFishNameCart);
            mFishPrice = itemView.findViewById(R.id.productFishPriceCart);
            mFishImage = itemView.findViewById(R.id.productFishCart);
            mFishTotalPrice = itemView.findViewById(R.id.productFishTotalPriceCart);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //get the position of clicked item
            int clickedPosition = getAdapterPosition();

            Cart mCart = newFishCart.get(clickedPosition);

            String fishName = mCart.getFishName();
            String quantity = mCart.getFishQuantity();
            String fishPriceEach = mCart.getFishpriceEach();
            String mFishTotalPrice = mCart.getFishTotalPrice();
            String contactNumber = mCart.getConctactNumber();
            String imageUrl = mCart.getFishImageUrl();
            String cartId = mCart.getCartId();

            mOnClickListener.onFishItemClick(fishName, quantity, fishPriceEach, mFishTotalPrice,contactNumber,imageUrl,cartId);

        }
    }

    //Interface defining our listener
    public interface CartItemClickListener {
        /**
         * Method that takes in 5 parameters to be passed to the new Activity
         *     String fishName;
         *     String fishImageUrl;
         *     String fishQuantity;
         *     String fishpriceEach;
         *     String fishTotalPrice;
         *     String cartId;
         *     String conctactNumber;
         *     String userId;
         * */

        void onFishItemClick(String fishName, String fishQuantity, String fishPriceEach, String totalPrice, String contactNumber, String ImageUrl, String cartId);
    }
}
