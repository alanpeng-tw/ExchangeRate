package com.alan.exchangerate.repository;

import com.alan.exchangerate.model.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface ExchangeRateRepository extends JpaRepository<ExchangeRate,Long> {


    @Query("SELECT new com.alan.exchangerate.model.ExchangeRate(e.currency_type,e.currency_type_name,e.rate,e.updated)from ExchangeRate e where e.currency_type = ?1")
    ExchangeRate findExchangeRateByCurrency_type(String currency_type);

    @Query("SELECT new com.alan.exchangerate.model.ExchangeRate(e.currency_type,e.currency_type_name,e.rate,e.updated)from ExchangeRate e where e.currency_type = ?1")
    Optional<ExchangeRate> updateExchangeRateByCurrency_type(String currency_type);

    @Modifying
    @Query("DELETE FROM ExchangeRate e where e.currency_type = :currency_type")
    int deleleByCurrency_type(@Param("currency_type") String currency_type);
}
