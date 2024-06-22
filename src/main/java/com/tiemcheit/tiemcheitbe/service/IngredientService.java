package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.IngredientRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IngredientRestockRequest;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.repository.IngredientRepo;
import com.tiemcheit.tiemcheitbe.repository.StoreRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IngredientService {
    private final IngredientRepo ingredientRepo;
    private final StoreRepo storeRepo;

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
        ingredient.setStore(storeRepo.getReferenceById(1L));
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

    public List<IngredientResponse> getIngredientsByStoreId(Long store_id) {
        return ingredientRepo.findAllByStoreId(store_id)
                .stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .collect(Collectors.toList());
    }
}
