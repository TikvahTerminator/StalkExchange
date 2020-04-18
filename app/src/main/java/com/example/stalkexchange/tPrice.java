package com.example.stalkexchange;

import com.example.stalkexchange.Users;

import java.util.Date;

public class tPrice {
    private Date transactionDate;
    private Integer price;
    private Users owner;

    public tPrice(Date date, Integer Bellprice, Users Owner){
        transactionDate = date;
        price = Bellprice;
        owner = Owner;

    }

    public Date getTransactionDate(){
        return transactionDate;
    }
    public Integer getPrice(){
        return price;
    }
    public Users getOwner(){
        return owner;
    }

}
