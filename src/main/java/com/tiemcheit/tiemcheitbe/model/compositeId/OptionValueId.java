package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Option;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

public class OptionValueId implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Option option;

    @Id
    private Long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionValueId that)) return false;
        return option.equals(that.option) && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return option.hashCode() + id.hashCode();
    }
}
