package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.IngredientRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IngredientRestockRequest;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.model.Ingredient;
import com.tiemcheit.tiemcheitbe.repository.IngredientRepo;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import com.tiemcheit.tiemcheitbe.service.specification.IngredientSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepo ingredientRepo;

    public List<IngredientResponse> getAllIngredients() {
        return ingredientRepo.findAll()
                .stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .collect(Collectors.toList());
    }

    public IngredientResponse create(IngredientRequest request) {
        if (ingredientRepo.findByName(request.getName()).isPresent()) {
            throw new AppException("Ingredient " + request.getName() + " already exists", HttpStatus.BAD_REQUEST);
        }
        var ingredient = IngredientMapper.INSTANCE.toIngredient(request);

        //comment when store service and mapper is available
        ingredient = ingredientRepo.save(ingredient);
        return IngredientMapper.INSTANCE.toIngredientResponse(ingredient);
    }

    public IngredientResponse update(long id, IngredientRequest request) {
        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new AppException("Ingredient " + request.getName() + " not found", HttpStatus.NOT_FOUND));

        ingredient.setName(request.getName());
        ingredient.setStatus(request.getStatus());
        ingredient.setQuantity(request.getQuantity());
        ingredient.setPrice(request.getPrice());
        ingredient.setImage(request.getImage());


        return IngredientMapper.INSTANCE.toIngredientResponse(ingredientRepo.save(ingredient));
    }

    public IngredientResponse disable(long id, IngredientRequest request) {
        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new AppException("Ingredient " + request.getName() + " already exists", HttpStatus.BAD_REQUEST));
        ingredient.setStatus("NOT AVAILABLE");
        return IngredientMapper.INSTANCE.toIngredientResponse(ingredientRepo.save(ingredient));

    }

    public IngredientResponse restock(long id, IngredientRestockRequest request) {
        var ingredient = ingredientRepo.findById(id)
                .orElseThrow(() -> new AppException("Ingredient already exists", HttpStatus.BAD_REQUEST));
        ingredient.setQuantity(ingredient.getQuantity() + request.getQuantity());
        return IngredientMapper.INSTANCE.toIngredientResponse(ingredientRepo.save(ingredient));
    }


    public IngredientResponse getIngredientById(Long id) {
        return ingredientRepo.findById(id).map(IngredientMapper.INSTANCE::toIngredientResponse).orElse(null);
    }
    public Page<IngredientResponse> getIngredientsWithPaginationAndSort(
            int page,
            int size,
            Map<String, String> conditions,
            String sortField,
            String sortDirection) {
        Specification<Ingredient> specification = IngredientSpecification.getSpecification(conditions);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField.trim());
        Page<Ingredient> ingredientPage = ingredientRepo.findAll(specification, PageRequest.of(page, size, sort));

        return ingredientPage.map(IngredientMapper.INSTANCE::toIngredientResponse);
    }
    public List<IngredientResponse> getAllAvailableIngredients() {
        return ingredientRepo.findAllByQuantityGreaterThan(10)
                .stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .collect(Collectors.toList());
    }
}
