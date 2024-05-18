package com.orpat.quickmix.api

import com.google.gson.JsonObject
import com.orpat.quickmix.fragment.CartResponseDataClass
import com.orpat.quickmix.model.*
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @GET("token")
    fun generateToken(): Call<TokenDataClass>

    @GET("user/refreshToken")
    fun generateRefreshToken(): Call<TokenDataClass>


    @GET("forceUpdate")
    fun forceUpdate(): Call<ForceUpdateModel>



    //Live Demo
    @FormUrlEncoded
    @POST("getTimeslots")
    fun getLiveDemoTimeSlots(@Field("date")  date:String): Call<TimeSlotDataClass>

    @FormUrlEncoded
    @POST("demo/getTimeslots")
    fun getDemoTimeSlots(@Field("date")  date:String): Call<TimeSlotDataClass>

    @FormUrlEncoded
    @POST("demo/scheduleDemo")
    fun scheduleDemo(
                    @Field("name") name:String,
                     @Field("email") email:String,
                    @Field("mobile_no") mobileNo:String,
                     @Field("date") date:String,
                     @Field("slot_id") slot_id:String,
                     ): Call<ResponseDataClass>

    @FormUrlEncoded
    @POST("demo/verifyOtpToScheduleDemo")
    fun verifyOtpToScheduleDemo(@Field("mobile_no") mobile_no:String,
                                @Field("otp") otp:String): Call<ResponseDataClass>

    @GET("products/getAllProducts")
    fun getAllProducts(): Call<AllProductsDataClass>

    @GET("products/singleProductDetails/{id}")
    fun getSingleProductDetails(@Path("id") id:Int,@Query("prod") prod:Int): Call<SingleProductDetailModel>


    //CART
    @FormUrlEncoded
    @POST("cart/addEditItems")
    fun addItemToCart(
            @Field("product_id") product_id:Int,
            @Field("quantity") quantity:Int,
            @Field("is_acccessories") is_acccessories:Int,
    ): Call<AllProductsDataClass>

    @POST("cart/getItems")
    fun getCartItems(): Call<AllProductsDataClass>

    @FormUrlEncoded
    @POST("orders/orderSummary")
    fun orderSummary(@Field("coupon_code") coupon_code:String?): Call<OrderSummaryDataClass>

    @GET("orders/orderListing")
    fun orderListing(): Call<OrderListingDataClass>

    @FormUrlEncoded
    @POST("orders/orderDetails")
    fun orderDetails(@Field("order_id") order_id:Int): Call<OrderDetailDataClass>


    //RECIPE
    @GET("recipe/getRecipeCategory")
    fun getRecipeCategory(): Call<RecipeCategoryDataClass>

    @FormUrlEncoded
    @POST("recipe/getRecipes")
    fun getRecipes(
        @Field("category_id") category_id:String?,
        @Field("recipe_id") recipe_id:String?,
        @Field("search") search:String?,
        @Field("page") page:Int,
    ): Call<RecipeDataClass>

    //Login
    @FormUrlEncoded
    @POST("user/login")
    fun loginUser(@Field("phone") phone:String): Call<LoginDataClass>

    @FormUrlEncoded
    @POST("/user/verifyOtp")
    fun verifyOtp(@Field("mobileNo") mobileNo:String,
                  @Field("otp") otp:String): Call<VerifyOtpDataClass>

    @FormUrlEncoded
    @POST("user/register")
    fun registerUser(@Field("mobile") mobileNo:String,
                     @Field("email") email:String,
                     @Field("name") name:String,): Call<RegisterDataClass>

    @FormUrlEncoded
    @POST("user/saveAddress")
    fun saveAddress(@Field("address1") address1:String,
                    @Field("address2") address2:String,
                    @Field("state_id") state_id:Int,
                    @Field("city_id") city_id:Int,
                    @Field("zip_code") zip_code:String): Call<AddressDataClass>

    @FormUrlEncoded
    @POST("user/getUserProfile")
    fun getUserProfile(@Field("customer_id") customer_id:String): Call<UserProfileDataClass>

    @FormUrlEncoded
    @POST("user/editUserProfile")
    fun editUserProfile(@Field("address1") address1:String,
                       @Field("address2") address2:String,
                       @Field("city_id") city_id:String,
                       @Field("state_id") state_id:String,
                       @Field("zip_code") zip_code:String): Call<AddressDataClass>

    @GET("user/getState")
    fun getState(): Call<StateDataClass>

    @GET("user/getCity/{id}")
    fun getCity(@Path("id") id:Int): Call<StateDataClass>

    //Payment

    @GET("coupon/getCoupons")
    fun getCoupons(): Call<DiscountCouponDataClass>

    @FormUrlEncoded
    @POST("orders/getPaymentLink")
    fun getPaymentLink(@Field("order_id") order_id:Int): Call<PaymentLinkDataClass>

    @FormUrlEncoded
    @POST("orders/processPaymentRequest")
    fun processPayment(@Field("checkout_id") order_id:Int,
                       @Field("payment_type") payment_type:Int,
                        ): Call<PaymentLinkDataClass>



}