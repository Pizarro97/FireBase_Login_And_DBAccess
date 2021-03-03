package com.joseapizarro.loginanddbacc;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ExpenseDef {

    private String date, expDetail, expName, expType;
    private float price;
    private int qty;

    public ExpenseDef(){}
    public ExpenseDef(String date, String expDetail, String expName, String expType, float price, int qty){
        this.date = date;
        this.expDetail = expDetail;
        this.expName = expName;
        this.expType = expType;
        this.price = price;
        this. qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpDetail() {
        return expDetail;
    }

    public void setExpDetail(String expDetail) {
        this.expDetail = expDetail;
    }

    public String getExpName() {
        return expName;
    }

    public void setExpName(String expName) {
        this.expName = expName;
    }

    public String getExpType() {
        return expType;
    }

    public void setExpType(String expType) {
        this.expType = expType;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("expDetail", expDetail);
        result.put("expName", expName);
        result.put("expType", expType);
        result.put("price", price);
        result.put("qty", qty);

        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Date: " + date + "\n" +
                "Name: " + expName + "\tType: " +
                expType + "\nDetail: " + expDetail +
                "\nPrice: " + price + "\tQty: " + qty;
    }
}
