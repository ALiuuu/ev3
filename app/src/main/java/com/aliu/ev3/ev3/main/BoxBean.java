package com.aliu.ev3.ev3.main;

import java.util.ArrayList;

/**
 * Created by ALiu on 2018/4/11.
 */
public class BoxBean {


    public ArrayList<ItemBean> clothes;
    public ArrayList<ItemBean> pants;
    public ArrayList<ItemBean> shoes;


    public BoxBean() {
        clothes = new ArrayList<>();
        pants = new ArrayList<>();
        shoes = new ArrayList<>();
    }


    public void putClothes(ItemBean itemBean) {
        clothes.add(itemBean);
    }

    public boolean getClothes(int i) {
        for (ItemBean clothe : clothes) {
            if (clothe.i == i) {
                clothes.remove(clothe);
                return true;
            }
        }
        return false;
    }

    public void putPants(ItemBean itemBean) {
        pants.add(itemBean);
    }

    public boolean getPants(int i) {
        for (ItemBean pant : pants) {
            if (pant.i == i) {
                pants.remove(pant);
                return true;
            }
        }
        return false;
    }

    public void putShoes(ItemBean itemBean) {
        shoes.add(itemBean);
    }

    public boolean getShoes(int i) {
        for (ItemBean clothe : shoes) {
            if (clothe.i == i) {
                shoes.remove(clothe);
                return true;
            }
        }
        return false;
    }

    public ItemBean get(int i){
        for (ItemBean pant : pants) {
            if (pant.i == i) {
                return pant;
            }
        }
        for (ItemBean pant : shoes) {
            if (pant.i == i) {
                return pant;
            }
        }
        for (ItemBean pant : clothes) {
            if (pant.i == i) {
                return pant;
            }
        }
        return null;
    }

    public int getFree() {

        for (int i = 1; i <= 8; i++) {
            if (!contain(clothes, i) && !contain(pants, i) && !contain(shoes, i)) {
                return i;
            }
        }

        return -1;
    }

    public void remove(int i ){
        getClothes(i);
        getPants(i);
        getShoes(i);
    }


    public static boolean contain(ArrayList<ItemBean> beans, int i) {
        if (beans.size() == 0) {
            return false;
        }
        for (ItemBean bean : beans) {
            if (bean.i == i) {
                return true;
            }
        }
        return false;
    }

    public static class ItemBean {

        public ItemBean(int i, String url,int res) {
            this.i = i;
            this.res = res;
            this.photourl = url;
        }

        public int    i;
        public int    res;
        public String photourl;

    }
}
