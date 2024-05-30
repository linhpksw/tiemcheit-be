package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.UserAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddressResponse;
import com.tiemcheit.tiemcheitbe.service.UserAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/address")
public class UserAddressController {
    private final UserAddressService userAddressService;

    @GetMapping("/{uid}")
    public ApiResponse<List<UserAddressResponse>> allUserAddress(@PathVariable Long uid) {
        var data = userAddressService.getAddressByUserId(uid);
        return ApiResponse.<List<UserAddressResponse>>builder().message("success").data(data).build();
    }

    @PostMapping("/{uid}")
    public ApiResponse<UserAddressResponse> addAddress(@PathVariable Long uid, @RequestBody UserAddressRequest body) {
        var data = userAddressService.addUserAddress(body, uid);
        return ApiResponse.<UserAddressResponse>builder().message("success").data(data).build();
    }
}
