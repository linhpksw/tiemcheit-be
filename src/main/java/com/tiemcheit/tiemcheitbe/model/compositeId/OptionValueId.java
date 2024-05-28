package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Option;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

public class OptionValueId implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;
    @Id
    private Long value_id;
}
