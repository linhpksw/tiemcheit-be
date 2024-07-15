package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface LogRepo extends JpaRepository<Log, Long> {
    @Query("SELECT l FROM Log l WHERE cast(l.timestamp as date) >= :startDate AND cast(l.timestamp as date) <= :endDate")
    Page<Log> findAllByDateRange(Pageable pageable, LocalDate startDate, LocalDate endDate);

    @Query("SELECT l FROM Log l WHERE cast(l.timestamp as date) >= :startDate AND cast(l.timestamp as date) <= :endDate AND l.responseStatus = :status")
    Page<Log> findByDateRangeAndStatus(Pageable pageable, LocalDate startDate, LocalDate endDate, Integer status);

    @Query("SELECT l FROM Log l WHERE cast(l.timestamp as date) >= :startDate AND cast(l.timestamp as date) <= :endDate AND l.responseStatus != :status")
    Page<Log> findByDateRangeAndStatusNot(Pageable pageable, LocalDate startDate, LocalDate endDate, Integer status);
}
