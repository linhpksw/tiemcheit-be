package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.OptionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.mapper.OptionMapper;
import com.tiemcheit.tiemcheitbe.model.Option;
import com.tiemcheit.tiemcheitbe.model.OptionValue;
import com.tiemcheit.tiemcheitbe.repository.OptionRepo;
import com.tiemcheit.tiemcheitbe.repository.OptionValueRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionService {
    private static final Logger log = LoggerFactory.getLogger(OptionService.class);
    private final OptionRepo optionRepo;
    private final OptionValueRepo optionValueRepo;

    private final OptionMapper optionMapper;

    public List<OptionResponse> getAllOptions() {
        return optionRepo.findAll()
                .stream()
                .map(optionMapper::toOptionResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public OptionResponse create(OptionRequest request) {
        final Option option = optionMapper.toOption(request);
        List<OptionValue> optionValues = option.getOptionValues().stream()
                .map(optionValue -> {
                    optionValue.setOption(option);
                    return optionValue;
                })
                .toList();
        log.info(optionValueRepo.toString());
        optionValueRepo.saveAll(optionValues);

        Option savedOption = optionRepo.save(option);
        return optionMapper.toOptionResponse(savedOption);
    }

}
