package com.paschburg.rich.popularmovies_v1;

/**
 * Created by richardpaschburg on 7/26/15.
 */
public class ImageItem {
    public String imagep;

    public ImageItem() {
        super();
    }

    public ImageItem(String imagep){
        super();
        this.imagep = imagep;
    }

    public String getImage() {
        return imagep;
    }

    public void setImage(String imagep) {
        this.imagep = imagep;
    }

}
