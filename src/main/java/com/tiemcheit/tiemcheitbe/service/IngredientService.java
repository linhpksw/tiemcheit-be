package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.repository.IngredientRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepo ingredientRepo;

    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepo.findAll()
                .stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .toList();
    }
}
