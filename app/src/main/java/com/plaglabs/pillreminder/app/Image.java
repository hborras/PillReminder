package com.plaglabs.pillreminder.app;

import java.util.List;

/**
 * Created by plagueis on 10/05/14.
 */
public class Image {
        private int id;
        private int rID;
        public List<Image> mImages;

        public List<Image> init(){


            return mImages;
        }
        public Image(int id, int rID) {
            this.id = id;
            this.rID = rID;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getrID() {
            return rID;
        }

        public void setrID(int rID) {
            this.rID = rID;
        }
    }
