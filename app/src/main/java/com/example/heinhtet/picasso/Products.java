package com.example.heinhtet.picasso;

/**
 * Created by heinhtet on 2/9/17.
 */

public class Products {
    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    String mName;
    String mPrice;
    String mImage;
    public Products(String name,String price, String image){
        mName = name;
        mImage = image;
        mPrice = price;


    }
}
