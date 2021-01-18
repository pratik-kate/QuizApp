package com.supertridents.learn.digital.marketing.quiz;

public class RecyclerModel {
    int level,s1,s2,s3;
    int lock;

    public RecyclerModel(){}
    public RecyclerModel(int level, int s1, int s2, int s3,int lock) {
        this.level = level;
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
        this.lock = lock;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getS1() {
        return s1;
    }

    public void setS1(int s1) {
        this.s1 = s1;
    }

    public int getS2() {
        return s2;
    }

    public void setS2(int s2) {
        this.s2 = s2;
    }

    public int getS3() {
        return s3;
    }

    public void setS3(int s3) {
        this.s3 = s3;
    }
}
