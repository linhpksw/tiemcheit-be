package com.tiemcheit.tiemcheitbe.mapper;

import com.tiemcheit.tiemcheitbe.dto.request.WishlistItemRequest;
import com.tiemcheit.tiemcheitbe.dto.response.WishlistItemResponse;
import com.tiemcheit.tiemcheitbe.model.WishlistItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface WishlistItemMapper {

    WishlistItem toEntity(WishlistItemRequest WishlistItemRequest);

    WishlistItemRequest toWishlistItemRequest(WishlistItem WishlistItem);

    WishlistItemResponse toWishlistItemResponse(WishlistItem WishlistItem);

    List<WishlistItemResponse> toWishlistItemResponses(List<WishlistItem> WishlistItems);

}
