package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.OptionValue;
import com.tiemcheit.tiemcheitbe.model.compositeId.OptionValueId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionValueRepo extends JpaRepository<OptionValue, OptionValueId> {
    void deleteAllByOptionId(Long option_id);
}
