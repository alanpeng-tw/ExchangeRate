package com.alan.exchangerate.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class ExchangeRate {
    public ExchangeRate(){

    }


    public ExchangeRate(String currency_type,String currency_type_name,String rate){
        this.currency_type = currency_type;
        this.currency_type_name = currency_type_name;
        this.rate = rate;
    }

    public ExchangeRate(String currency_type,String currency_type_name,String rate, String updated){
        this.currency_type = currency_type;
        this.currency_type_name = currency_type_name;
        this.rate = rate;
        this.updated = updated;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    //幣別
    //@Id
    @Column(name="currency_type", unique = true)
    private String currency_type;

    //幣別名稱
    @Column(name="currency_type_name")
    private String currency_type_name;

    //匯率
    @Column(name = "rate")
    private String rate;

    //更新時間
    @Column(name="updated")
    private String updated;

    //建立時間
    //@Column(name = "create_time",insertable = false,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    //private Date createTime;

    //更新時間
    //@Column(name = "update_time",insertable = false,updatable = false,columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    //private Date updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getCurrency_type_name() {
        return currency_type_name;
    }

    public void setCurrency_type_name(String currency_type_name) {
        this.currency_type_name = currency_type_name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }
}
