package com.alan.exchangerate.controller;

import com.alan.exchangerate.model.ExchangeRate;
import com.alan.exchangerate.repository.ExchangeRateRepository;
import com.alan.exchangerate.service.ExchangeRateService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ExchangeRateController {
    @Autowired
    ExchangeRateService exchangeRateService;
    @Autowired
    ExchangeRateRepository exchangeRateRepository;

    //實作第2點
    //測試第5點
    //return coindesk API的json 字串
    @RequestMapping(value = "/getCoindeskApi")
    public String getCoindesk() {
        String jsonStr = "";
        try{
            return exchangeRateService.getCurrentPriceApi();
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonStr;
    }

    //實作第3點
    @RequestMapping(value = "/getCoindeskApiConverter")
    public ResponseEntity<List<ExchangeRate>> getCoindeskApiConverter(){

        String jsonStr = "";

        try{
            jsonStr = exchangeRateService.getCurrentPriceApi();
            if(StringUtils.isNotEmpty(jsonStr)){
                return exchangeRateService.saveExchangeRateData(jsonStr);
            }else{
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //測試第1點
    @GetMapping("/exchangeRates/{currency_type}")
    public ResponseEntity<ExchangeRate> getExchangeRateByCurrency_type(@PathVariable("currency_type") String currency_type){


        try{
            ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByCurrency_type(currency_type);
            return new ResponseEntity<>(exchangeRate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //測試第6點
    @GetMapping("/exchangeRates")
    public ResponseEntity<List<ExchangeRate>> getAllCoindesks(){
        try{
            List<ExchangeRate> coindeskList = new ArrayList<ExchangeRate>();
            exchangeRateRepository.findAll().forEach(coindeskList::add);

            if(coindeskList.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(coindeskList,HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
        測試第2點

        input:
            currency_type      幣別
            currency_type_name 幣別名稱
            rate               匯率
     */
    @PostMapping("/exchangeRates")
    public ResponseEntity<ExchangeRate> createExchangeRate(@RequestBody ExchangeRate exchangeRate){
        try{

            ExchangeRate _exchangeRate = exchangeRateRepository
                    .save(new ExchangeRate(
                            exchangeRate.getCurrency_type(),
                            exchangeRate.getCurrency_type_name(),
                            exchangeRate.getRate(),
                            exchangeRateService.getLocalTime()
                    ));


            return new ResponseEntity<>(_exchangeRate,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*
        測試第3點

        input:
            id
            currency_type
            currency_type_name
            rate
     */
    @PutMapping("/exchangeRates/{currency_type}")
    public ResponseEntity<ExchangeRate> updateExchangeRate(@PathVariable("currency_type") String currency_type, @RequestBody ExchangeRate exchangeRate){

        try{
            //Optional<ExchangeRate> exchangeRateData = exchangeRateRepository.findExchangeRateByCurrency_type(currency_type);
            Optional<ExchangeRate> exchangeRateData = exchangeRateRepository.updateExchangeRateByCurrency_type(currency_type);
            if(exchangeRateData.isPresent()){
                ExchangeRate _exchangeRate = exchangeRateData.get();
                //_exchangeRate.setCurrency_type(exchangeRate.getCurrency_type());
                _exchangeRate.setCurrency_type_name(exchangeRate.getCurrency_type_name());
                _exchangeRate.setRate(exchangeRate.getRate());
                _exchangeRate.setUpdated(exchangeRateService.getLocalTime());
                //update
                return new ResponseEntity<>(exchangeRateRepository.save(_exchangeRate), HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    /*
        測試第4點

        input: id
    */
    @DeleteMapping("/exchangeRates/{id}")
    public ResponseEntity<HttpStatus> deleteExchangeRates(@PathVariable("id") Long id){
        try{
            exchangeRateRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
