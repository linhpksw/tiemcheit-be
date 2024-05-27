package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.UserAddressDto;
import com.tiemcheit.tiemcheitbe.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    @GetMapping("/address/userId={uid}")
    public ResponseEntity<List<UserAddressDto>> allCartItems(@PathVariable Long uid) {
        return ResponseEntity.ok(userAddressService.getAddressByUserId(uid));
    }
}
