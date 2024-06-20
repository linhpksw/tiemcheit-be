package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.dto.response.GoogleUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-client", url = "https://www.googleapis.com")
public interface UserClient {
    @GetMapping(value = "/oauth2/v1/userinfo", produces = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    GoogleUserResponse getGoogleUserInfo(@RequestParam("alt") String alt, @RequestParam("access_token") String accessToken);
}
