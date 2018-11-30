package com.auto.di.guan.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/7/9.
 */

public class ImageInfo {
    public String groupName;
    public List<Child>childs;

    public static class Child {
        public int isSel;
        public int imageId;
        public int imageDir;
        public int[] array;

        public Child(int isSel, int[] array, int pipeType) {
            this.isSel = isSel;
            this.array = array;
            this.pipeType = pipeType;
        }

        public int pipeType;
        public Child(int isSel, int[] array) {
            this.isSel = isSel;
            this.array = array;
        }
    }
}
