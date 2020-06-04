package com.adam.annuairechiali.Model;

import android.widget.ImageView;

import java.io.Serializable;

public class ImageV implements Serializable {
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    private ImageView imageView;
    private String pic;

    public ImageV(ImageView imageView, String pic) {
        this.imageView = imageView;
        this.pic = pic;
    }


}
