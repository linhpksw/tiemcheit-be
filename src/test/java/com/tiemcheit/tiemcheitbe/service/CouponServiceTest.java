package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CouponRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.CouponResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.CouponMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//@ExtendWith(SpringExtension.class)
class CouponServiceTest {

    @Mock
    private CouponMapper couponMapper;
    @Mock
    private ProductMapper productMapper;
    @Mock
    private CouponRepo couponRepository;
    @Mock
    private CategoryRepo categoryRepository;
    @Mock
    private ProductRepo productRepository;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private CartService cartService;
    @Mock
    private UserRepo userRepo;


    @InjectMocks
    private CouponService couponService;

    private CouponRequest validCouponRequest;
    private CouponRequest invalidCouponRequest;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        validCouponRequest = new CouponRequest();
        validCouponRequest.setName("Valid Name");
        validCouponRequest.setCode("DUPLICATECODE");
        validCouponRequest.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        validCouponRequest.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48));
        validCouponRequest.setDescription("Valid Description");
        validCouponRequest.setLimitAccountUses(5);
        validCouponRequest.setLimitUses(10);
        validCouponRequest.setDiscounts(Collections.emptyList()); // Ensure discounts list is initialized

    }

    // 1. Valid coupon validation (all fields valid)
    @Test
    void testValidateCouponRequest_AllFieldsValid() {
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list
        assertDoesNotThrow(() -> couponService.validateCouponRequest(request));
    }

    // 1. Valid coupon validation (all fields valid)
    @Test
    void testValidateCouponRequest_NameBoundary1() {
        CouponRequest request = new CouponRequest();
        request.setName("Abcd");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list
        assertDoesNotThrow(() -> couponService.validateCouponRequest(request));
    }

    // 1. Valid coupon validation (all fields valid)
    @Test
    void testValidateCouponRequest_NameBoundary2() {
        String longName = "A".repeat(64);
        CouponRequest request = new CouponRequest();
        request.setName(longName);
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list
        assertDoesNotThrow(() -> couponService.validateCouponRequest(request));
    }

    // 1. Valid coupon validation (all fields valid)
    @Test
    void testValidateCouponRequest_NameBoundary3() {
        String longName = "A".repeat(65);
        CouponRequest request = new CouponRequest();
        request.setName(longName);
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Tên không hợp lệ", exception.getMessage());
    }

    // 1. Valid coupon validation (all fields valid)
    @Test
    void testValidateCouponRequest_NameBoundary4() {
        CouponRequest request = new CouponRequest();
        request.setName("abc");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Tên không hợp lệ", exception.getMessage());
    }

    // 2. Invalid coupon validation (invalid code)
    @Test
    void testValidateCouponRequest_InvalidCode() {
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("invalid code"); // invalid code
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Mã không hợp lệ", exception.getMessage());
    }

    // 3. Invalid coupon validation (invalid name)
    @Test
    void testValidateCouponRequest_InvalidName() {
        CouponRequest request = new CouponRequest();
        request.setName("     "); // invalid name
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Tên không hợp lệ", exception.getMessage());
    }

    // 4. Invalid coupon validation (expired date valid)
    @Test
    void testValidateCouponRequest_InvalidDateValid() {
        CouponRequest request = new CouponRequest();
        request.setName("valid name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() - 86400000)); // -1 day
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Date Valid is required and must be in the future", exception.getMessage());
    }

    // 5. Invalid coupon validation (expired date expired)
    @Test
    void testValidateCouponRequest_ExpiredDateExpired() {
        CouponRequest request = new CouponRequest();
        request.setName("valid name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 86400000)); // +1 day
        request.setDateExpired(new Date(System.currentTimeMillis() - 86400000)); // -1 day
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Date Expired is required and must be after Date Valid", exception.getMessage());
    }

    // 6. Invalid coupon validation (empty description)
    @Test
    void testValidateCouponRequest_EmptyDescription() {
        CouponRequest request = new CouponRequest();
        request.setName("valid name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48));
        request.setDescription(""); //
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Vui lòng nhập mô tả", exception.getMessage());
    }

    // 7. Invalid coupon validation (limit account uses < 1)
    @Test
    void testValidateCouponRequest_LimitAccountUsesLessThanOne() {
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(0);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Limit Account Uses is required and must be greater than 0", exception.getMessage());
    }

    // 8. Invalid coupon validation (limit uses < 1)
    @Test
    void testValidateCouponRequest_LimitUsesLessThanOne() {
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(0);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Limit Uses is required and must be greater than 0", exception.getMessage());
    }

    // 9. Invalid coupon validation (limit uses < 1)
    @Test
    void testValidateCouponRequest_InvalidName2() {
        CouponRequest request = new CouponRequest();
        request.setName("123123");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(0);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Tên không được chứa toàn ký tự sô", exception.getMessage());
    }

    // 10. Invalid coupon validation (limit uses < 1)
    @Test
    void testValidateCouponRequest_InvalidCode2() {
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("9999");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(0);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(request));
        assertEquals("Mã không được chứa toàn ký tự sô", exception.getMessage());
    }

    // 39. Validate coupon request (null request)
    @Test
    void testValidateCouponRequest_NullRequest() {
        AppException exception = assertThrows(AppException.class, () -> couponService.validateCouponRequest(null));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    // 11. Valid coupon application to cart (total discount)
    @Test
    void testApplyCouponToCart_TotalDiscount1() {
        List<CartItemResponse> cartItems = new ArrayList<>();
        CartItemResponse cartItem = new CartItemResponse();
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setPrice(100.0);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItems.add(cartItem);

        User user = new User();
        user.setUsername("linhpksw");

        Coupon coupon = new Coupon();
        coupon.setCode("ABCD1234");
        coupon.setLimitAccountUses(5);
        coupon.setLimitUses(100);

        Discount discount = new Discount();
        discount.setType("total");
        discount.setValueType("percent");
        discount.setValueFixed(5.0);

        coupon.setDiscounts(Collections.singletonList(discount));

        when(cartService.allCartItems()).thenReturn(cartItems);
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        double discountAmount = couponService.applyCouponToCart("ABCD1234", user.getUsername());

        assertEquals(5, discountAmount);
    }

    // 12. Valid coupon application to cart (total discount)
    @Test
    void testApplyCouponToCart_TotalDiscount2() {
        List<CartItemResponse> cartItems = new ArrayList<>();
        CartItemResponse cartItem = new CartItemResponse();
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setPrice(100.0);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItems.add(cartItem);

        User user = new User();
        user.setUsername("linhpksw");

        Coupon coupon = new Coupon();
        coupon.setLimitUses(1);
        coupon.setLimitAccountUses(1);
        coupon.setCode("ABCD1234");

        Discount discount = new Discount();
        discount.setType("total");
        discount.setValueType("fixed");
        discount.setValueFixed(5000.0);

        coupon.setDiscounts(Collections.singletonList(discount));

        when(cartService.allCartItems()).thenReturn(cartItems);
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        double discountAmount = couponService.applyCouponToCart("ABCD1234", user.getUsername());

        assertEquals(5000, discountAmount);
    }

    @Test
    void testApplyCouponToCart_LimitAccountUseBoundary() {
        List<CartItemResponse> cartItems = new ArrayList<>();
        CartItemResponse cartItem = new CartItemResponse();
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setPrice(100.0);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItems.add(cartItem);

        User user = new User();
        user.setUsername("linhpksw");

        Coupon coupon = new Coupon();
        coupon.setCode("ABCD1234");
        coupon.setLimitAccountUses(1);
        coupon.setLimitUses(100);

        Discount discount = new Discount();
        discount.setType("total");
        discount.setValueType("percent");
        discount.setValueFixed(5.0);

        coupon.setDiscounts(Collections.singletonList(discount));

        when(cartService.allCartItems()).thenReturn(cartItems);
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        double discountAmount = couponService.applyCouponToCart("ABCD1234", user.getUsername());

        assertEquals(5, discountAmount);
    }

    @Test
    void testApplyCouponToCart_LimitUseBoundary() {
        List<CartItemResponse> cartItems = new ArrayList<>();
        CartItemResponse cartItem = new CartItemResponse();
        ProductResponse product = new ProductResponse();
        product.setId(1L);
        product.setPrice(100.0);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItems.add(cartItem);

        User user = new User();
        user.setUsername("linhpksw");

        Coupon coupon = new Coupon();
        coupon.setCode("ABCD1234");
        coupon.setLimitAccountUses(1);
        coupon.setLimitUses(1);

        Discount discount = new Discount();
        discount.setType("total");
        discount.setValueType("percent");
        discount.setValueFixed(5.0);

        coupon.setDiscounts(Collections.singletonList(discount));

        when(cartService.allCartItems()).thenReturn(cartItems);
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        double discountAmount = couponService.applyCouponToCart("ABCD1234", user.getUsername());

        assertEquals(5, discountAmount);
    }

    // 2. Apply coupon with limit account uses (exceeds limit)
    @Test
    void testApplyCouponToCart_LimitAccountUsesExceedsLimit() {
        ProductResponse product1 = new ProductResponse();
        product1.setId(1L);
        product1.setPrice(100.0);

        ProductResponse product2 = new ProductResponse();
        product2.setId(2L);
        product2.setPrice(1000.0);

        CartItemResponse cartItem1 = new CartItemResponse();
        cartItem1.setProduct(product1);
        cartItem1.setQuantity(1);

        CartItemResponse cartItem2 = new CartItemResponse();
        cartItem2.setProduct(product2);
        cartItem2.setQuantity(2);
        List<CartItemResponse> cartItems = Arrays.asList(
                cartItem1, cartItem2
        );


        User user = new User();
        user.setId(1L);
        user.setUsername("linhpksw");

        Coupon coupon = new Coupon();
        coupon.setId(1L);
        coupon.setCode("ABCD1234");
        coupon.setLimitAccountUses(1);
        coupon.setLimitUses(100);

        Discount discount = new Discount();
        discount.setType("total");
        discount.setValueType("fixed");
        discount.setValueFixed(5000.0);

        coupon.setDiscounts(Collections.singletonList(discount));
        // Debugging statements
        System.out.println("Setting up mocks...");

        when(cartService.allCartItems()).thenReturn(cartItems);
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);
        when(userRepo.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(orderRepo.findByUserIdAndCouponId(anyLong(), anyLong())).thenReturn(Arrays.asList(new Order(), new Order()));

        AppException exception = assertThrows(AppException.class, () -> couponService.applyCouponToCart("ABCD1234", user.getUsername()));
        assertEquals("You have access the user's limit uses", exception.getMessage());
    }

    // 11. Invalid coupon application to cart (usage limits exceeded)
    @Test
    void testApplyCouponToCart_UsageLimitsExceeded() {
        Coupon coupon = new Coupon();
        coupon.setLimitUses(1);
        coupon.setUseCount(1);
        User user = new User();
        user.setUsername("linhpksw");
        when(couponRepository.findByCode("ABCD1234")).thenReturn(coupon);

        AppException exception = assertThrows(AppException.class, () -> couponService.applyCouponToCart("ABCD1234", user.getUsername()));
        assertEquals("Coupon is not valid anymore", exception.getMessage());
    }

    // 13. Invalid coupon application to cart (coupon not found)
    @Test
    void testApplyCouponToCart_CouponNotFound() {
        User user = new User();
        user.setUsername("linhpksw");

        when(couponRepository.findByCode("INVALID")).thenReturn(null);

        AppException exception = assertThrows(AppException.class, () -> couponService.applyCouponToCart("123434", user.getUsername()));
        assertEquals("Coupon not found", exception.getMessage());
    }

    // 14. Apply product discount (normal input)
    @Test
    void testApplyProductDiscount_ProductTypePercent() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        Category category = new Category();
        category.setId(1L);

        product.setCategory(category);
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setPrice(100.0);
        response.setCategory(category);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("product");
        discount.setValueType("percent");
        discount.setValueFixed(10.0);
        discount.setProduct(product);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, response);

        assertEquals(10, discountAmount);
    }

    // 15. Apply product discount (normal input)
    @Test
    void testApplyProductDiscount_ProductTypeFixed() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        Category category = new Category();
        category.setId(1L);

        product.setCategory(category);
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setPrice(100.0);
        response.setCategory(category);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("product");
        discount.setValueType("fixed");
        discount.setValueFixed(4000.0);
        discount.setProduct(product);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, response);

        assertEquals(4000, discountAmount);
    }

    // 16. Apply product discount (normal input)
    @Test
    void testApplyProductDiscount_CategoryTypePercent() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        Category category = new Category();
        category.setId(1L);

        product.setCategory(category);
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setPrice(100.0);
        response.setCategory(category);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("category");
        discount.setValueType("percent");
        discount.setValueFixed(10.0);
        discount.setCategory(category);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, response);

        assertEquals(10, discountAmount);
    }

    // 17. Apply product discount (normal input)
    @Test
    void testApplyProductDiscount_CategoryTypeFixed() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(100.0);

        Category category = new Category();
        category.setId(1L);

        product.setCategory(category);
        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setPrice(100.0);
        response.setCategory(category);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("product");
        discount.setValueType("fixed");
        discount.setValueFixed(4000.0);
        discount.setProduct(product);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, response);

        assertEquals(4000, discountAmount);
    }

    // 18. Apply product discount (no applicable discount)
    @Test
    void testApplyProductDiscount_NoApplicableDiscount() {
        Category category = new Category();
        category.setId(1L);

        Category category2 = new Category();
        category.setId(2L);

        ProductResponse response = new ProductResponse();
        response.setId(1L);
        response.setPrice(100.0);
        response.setCategory(category);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("category");
        discount.setValueType("percent");
        discount.setValueFixed(10.0);
        discount.setCategory(category2);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, response);

        assertEquals(0, discountAmount);
    }

    // 35. Apply product discount (product not found in discounts)
    @Test
    void testApplyProductDiscount_ProductNotFoundInDiscounts() {
        ProductResponse product = new ProductResponse();
        product.setId(99L);
        product.setName("Nonexistent Product");
        product.setPrice(200.0);

        Product productExist = new Product();
        productExist.setId(1L);
        productExist.setName("Product");
        productExist.setPrice(100.0);

        List<Discount> discounts = new ArrayList<>();
        Discount discount = new Discount();
        discount.setType("product");
        discount.setValueType("percent");
        discount.setValueFixed(10.0);
        discount.setProduct(productExist);
        discounts.add(discount);

        double discountAmount = couponService.applyProductDiscount(discounts, product);

        assertEquals(0, discountAmount);
    }

    // 1. Valid coupon creation
    @Test
    void testCreateCoupon_NormalInput() {
        // Arrange
        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("VALIDCODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        Coupon coupon = new Coupon();
        when(couponRepository.save(any(Coupon.class))).thenReturn(coupon);
        when(couponMapper.toResponse(any(Coupon.class))).thenReturn(new CouponResponse());

        // Act
        CouponResponse response = couponService.createCoupon(request);

        // Assert
        assertNotNull(response);
        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    void testCreateCouponAndThenDuplicate() {
        Coupon savedCoupon = new Coupon();
        savedCoupon.setId(1L);
        savedCoupon.setCode("DUPLICATECODE");

        CouponRequest request = new CouponRequest();
        request.setName("Valid Name");
        request.setCode("DUPLICATECODE");
        request.setDateValid(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)); // 1 day in the future
        request.setDateExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 48)); // 2 days in the future
        request.setDescription("Valid Description");
        request.setLimitAccountUses(5);
        request.setLimitUses(10);
        request.setDiscounts(new ArrayList<>()); // Empty discounts list

        when(couponRepository.save(any(Coupon.class)))
                .thenReturn(savedCoupon)
                .thenThrow(new DataIntegrityViolationException("Duplicate code"));

        // First insertion
        couponService.createCoupon(request);
        
        // Attempt to insert a duplicate
        AppException thrown = assertThrows(AppException.class, () -> {
            couponService.createCoupon(request);
        });

        assertEquals("Coupon code must be unique", thrown.getMessage());
    }
}




