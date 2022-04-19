package com.alan.exchangerate.service;


import com.alan.exchangerate.model.*;
import com.alan.exchangerate.repository.ExchangeRateRepository;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ExchangeRateService {

    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    private final String apiUrl = "https://api.coindesk.com/v1/bpi/currentprice.json";

    //抓取原始JSON
    public String getCurrentPriceApi() throws Exception{

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(apiUrl);

        CloseableHttpResponse response = null;

        String content = "";

        response = httpClient.execute(httpGet);

        //成功
        if(response.getStatusLine().getStatusCode() == 200){
            content = EntityUtils.toString(response.getEntity(),"UTF-8");
        }

        return content;
    }

    //解析JSON
    private CurrentPrice parsingJsonAgain(String content){

        CurrentPrice currentPrice = null;

        try{

            currentPrice = new CurrentPrice();

            //第一層
            JSONObject first = JSONObject.fromObject(content);
            System.out.println("=========解析第一層=========");
            System.out.println("disclaimer: " + first.get("disclaimer"));
            System.out.println("chartName: " + first.get("chartName"));
            //set
            currentPrice.setDisclaimer(first.get("disclaimer").toString());
            currentPrice.setChartName(first.get("chartName").toString());

            JSONObject secondObjectTime = JSONObject.fromObject(first.getString("time"));
            System.out.println("=========解析第二層=========");
            System.out.println("updated: " + secondObjectTime.get("updated"));
            System.out.println("updatedISO: " + secondObjectTime.get("updatedISO"));
            System.out.println("updateduk: " + secondObjectTime.get("updateduk"));
            //set
            Time time = new Time();
            time.setUpdated(secondObjectTime.get("updated").toString());
            time.setUpdatedISO(secondObjectTime.get("updatedISO").toString());
            time.setUpdateduk(secondObjectTime.get("updateduk").toString());
            currentPrice.setTime(time);


            //bpi
            JSONObject secondObjectBpi = JSONObject.fromObject(first.getString("bpi"));
            Bpi bpi = new Bpi();

            //USD

            System.out.println("=========解析第三層=========");
            JSONObject thirdObjectUSD = JSONObject.fromObject(secondObjectBpi.getString("USD"));
            System.out.println("code: " + thirdObjectUSD.get("code"));
            System.out.println("symbol: " + thirdObjectUSD.get("symbol"));
            System.out.println("rate: " + thirdObjectUSD.get("rate"));
            System.out.println("description: " + thirdObjectUSD.get("description"));
            System.out.println("rate_float: " + thirdObjectUSD.get("rate_float"));
            //set
            Usd usd = new Usd();
            usd.setCode(thirdObjectUSD.get("code").toString());
            usd.setSymbol(thirdObjectUSD.get("symbol").toString());
            usd.setRate(thirdObjectUSD.get("rate").toString());
            usd.setDescription(thirdObjectUSD.get("description").toString());
            usd.setRate_float(Float.parseFloat(thirdObjectUSD.get("rate_float").toString()));
            bpi.setUsd(usd);

            //GBP
            JSONObject thirdObjectGBP = JSONObject.fromObject(secondObjectBpi.getString("GBP"));
            System.out.println("code: " + thirdObjectGBP.get("code"));
            System.out.println("symbol: " + thirdObjectGBP.get("symbol"));
            System.out.println("rate: " + thirdObjectGBP.get("rate"));
            System.out.println("description: " + thirdObjectGBP.get("description"));
            System.out.println("rate_float: " + thirdObjectGBP.get("rate_float"));
            //set
            Gbp gbp = new Gbp();
            gbp.setCode(thirdObjectGBP.get("code").toString());
            gbp.setSymbol(thirdObjectGBP.get("symbol").toString());
            gbp.setRate(thirdObjectGBP.get("rate").toString());
            gbp.setDescription(thirdObjectGBP.get("description").toString());
            gbp.setRate_float(Float.parseFloat(thirdObjectGBP.get("rate_float").toString()));
            bpi.setGbp(gbp);

            //EUR
            JSONObject thirdObjectEUR = JSONObject.fromObject(secondObjectBpi.getString("EUR"));
            System.out.println("code: " + thirdObjectGBP.get("code"));
            System.out.println("symbol: " + thirdObjectGBP.get("symbol"));
            System.out.println("rate: " + thirdObjectGBP.get("rate"));
            System.out.println("description: " + thirdObjectGBP.get("description"));
            System.out.println("rate_float: " + thirdObjectGBP.get("rate_float"));
            //set
            Eur eur = new Eur();
            eur.setCode(thirdObjectEUR.get("code").toString());
            eur.setSymbol(thirdObjectEUR.get("symbol").toString());
            eur.setRate(thirdObjectEUR.get("rate").toString());
            eur.setDescription(thirdObjectEUR.get("description").toString());
            eur.setRate_float(Float.parseFloat(thirdObjectEUR.get("rate_float").toString()));
            bpi.setEur(eur);

            currentPrice.setBpi(bpi);


        }catch(Exception e){
            e.printStackTrace();
        }

        return currentPrice;
    }

    public ResponseEntity<List<ExchangeRate>> saveExchangeRateData(String jsonData){

        CurrentPrice currentPrice = null;
        List<ExchangeRate> exchangeRateList = null;
        try{
            exchangeRateList = new ArrayList<>();

            currentPrice = parsingJsonAgain(jsonData);
            if(currentPrice != null){

                //美金
                exchangeRateList.add( new ExchangeRate(
                        currentPrice.getBpi().getUsd().getCode(),
                        "美金",
                        currentPrice.getBpi().getUsd().getRate(),
                        this.getLocalTime()
                ));

                //英鎊
                exchangeRateList.add( new ExchangeRate(
                        currentPrice.getBpi().getGbp().getCode(),
                        "英鎊",
                        currentPrice.getBpi().getGbp().getRate(),
                        this.getLocalTime()
                ));


                //歐元
                exchangeRateList.add( new ExchangeRate(
                        currentPrice.getBpi().getEur().getCode(),
                        "歐元",
                        currentPrice.getBpi().getEur().getRate(),
                        this.getLocalTime()
                ));


                List<ExchangeRate> _coindeskList = exchangeRateRepository.saveAll(exchangeRateList);
                return new ResponseEntity<>(_coindeskList, HttpStatus.OK);
            }

            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);

        }catch(Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String getLocalTime() throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateformat = formatter.format(new Date());

        return dateformat;
    }

}
