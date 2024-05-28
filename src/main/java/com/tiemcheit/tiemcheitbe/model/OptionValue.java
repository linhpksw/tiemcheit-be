package com.tiemcheit.tiemcheitbe.model;

import com.tiemcheit.tiemcheitbe.model.compositeId.OptionValueId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "option_values")
@IdClass(OptionValueId.class)
public class OptionValue {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Id
    private Long value_id;

    private String value_name;
}
