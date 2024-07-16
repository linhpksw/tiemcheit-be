package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Long> {
    @Query("SELECT f FROM Feedback f " +
            "WHERE (:startDate IS NULL OR f.sentAt >= :startDate)" +
            "AND (:endDate IS NULL OR f.sentAt <= :endDate)")
    List<Feedback> findAllByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
