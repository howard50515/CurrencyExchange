package com.example.test;

import android.annotation.SuppressLint;

public class Currency {
    public String name = "";
    public float bankNoteBuyingRate;
    public float bankNoteSellingRate;
    public float spotBuyingRate;
    public float spotSellingRate;
    public float rate;

    public Currency(String name, String bankNoteBuyingRate, String bankNoteSellingRate, String spotBuyingRate, String spotSellingRate){
        this.name = name;
        this.bankNoteBuyingRate = getCheckedCurrency(bankNoteBuyingRate);
        this.bankNoteSellingRate = getCheckedCurrency(bankNoteSellingRate);
        this.spotBuyingRate = getCheckedCurrency(spotBuyingRate);
        this.spotSellingRate = getCheckedCurrency(spotSellingRate);
        int dividend = 0;
        if (this.bankNoteBuyingRate != -1) {
            this.rate += this.bankNoteBuyingRate;
            dividend++;
        }
        if (this.bankNoteSellingRate != -1) {
            this.rate += this.bankNoteSellingRate;
            dividend++;
        }
        if (this.spotBuyingRate != -1) {
            this.rate += this.spotBuyingRate;
            dividend++;
        }
        if (this.spotSellingRate != -1) {
            this.rate += this.spotSellingRate;
            dividend++;
        }
        if (dividend != 0)
            this.rate /= dividend;
    }

    public float getCheckedCurrency(String rate){
        try{
            return Float.parseFloat(rate);
        } catch (NumberFormatException ex){
            return -1;
        }
    }

    @SuppressLint("DefaultLocale")
    @Override
    public String toString(){
        return String.format("%s %.2f %.2f %.2f %.2f",
                name, bankNoteBuyingRate, bankNoteSellingRate, spotBuyingRate, spotSellingRate);
    }
}
