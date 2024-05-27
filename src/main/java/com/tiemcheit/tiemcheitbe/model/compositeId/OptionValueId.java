package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Option;
import jakarta.persistence.Id;

import java.io.Serializable;

public class OptionValueId implements Serializable {
    @Id
    private Option option;
    @Id
    private Long value_id;
}
