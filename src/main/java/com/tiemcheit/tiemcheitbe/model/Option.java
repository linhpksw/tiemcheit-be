package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "options")
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "option", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<ProductOption> productOptions;

    @OneToMany(mappedBy = "option", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<OptionValue> optionValues;
}
