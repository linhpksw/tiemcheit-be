package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.mapper.OptionMapper;
import com.tiemcheit.tiemcheitbe.repository.OptionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final OptionRepo optionRepo;

    private final OptionMapper optionMapper;

    public List<OptionResponse> getAllOptions() {
        return optionRepo.findAll()
                .stream()
                .map(optionMapper::toOptionResponse)
                .toList();
    }
}
