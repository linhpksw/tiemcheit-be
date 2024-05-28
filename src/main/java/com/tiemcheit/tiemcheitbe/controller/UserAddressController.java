package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.UserAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddressResponse;
import com.tiemcheit.tiemcheitbe.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserAddressController {
    private final UserAddressService userAddressService;

    @GetMapping("/address/userId={uid}")
    public ResponseEntity<List<UserAddressResponse>> allUserAddress(@PathVariable Long uid) {
        return ResponseEntity.ok(userAddressService.getAddressByUserId(uid));
    }

    @PostMapping("/address/userId={uid}")
    public ResponseEntity<UserAddressResponse> addAddress(@PathVariable Long uid, @RequestBody UserAddressRequest body) {
        return ResponseEntity.ok(userAddressService.addUserAddress(body, uid));
    }
}
