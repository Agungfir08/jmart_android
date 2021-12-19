package com.AgungJmartAK.jmart_android.model;

import java.util.ArrayList;
import java.util.Date;

public class Payment extends Invoice{
    public int productCount;
    public Shipment shipment;

    public ArrayList<Record> history = new ArrayList<Record>();

    public Payment(int buyerId, int productId, int productCount, Shipment shipment){
        super(buyerId,productId);
        this.productCount = productCount;
        this.shipment = shipment;
    }

    @Override
    public double getTotalPay(Product product) {
        return product.price*((100.0-product.discount)/100)*productCount + shipment.cost;
    }

    public static class Record{
        public Status status;
        public final Date date = null;
        public String message;

        public Record(Status status, String message){
            this.status = status;
            this.message = message;
        }
    }
}

