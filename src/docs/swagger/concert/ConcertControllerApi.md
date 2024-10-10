# ConcertControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getConcertDates**](ConcertControllerApi.md#getConcertDates) | **GET** /concert/{concertId} | 
[**getConcertSeats**](ConcertControllerApi.md#getConcertSeats) | **GET** /concert/{concertId}/{date} | 
[**paySeat**](ConcertControllerApi.md#paySeat) | **POST** /concert/{userId}/payments | 
[**reserveSeat**](ConcertControllerApi.md#reserveSeat) | **POST** /concert/{userId}/reserve | 

<a name="getConcertDates"></a>
# **getConcertDates**
> ApiResponseListGetConcertDatesResponse getConcertDates(concertId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConcertControllerApi;


ConcertControllerApi apiInstance = new ConcertControllerApi();
Long concertId = 789L; // Long | 
try {
    ApiResponseListGetConcertDatesResponse result = apiInstance.getConcertDates(concertId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConcertControllerApi#getConcertDates");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **concertId** | **Long**|  |

### Return type

[**ApiResponseListGetConcertDatesResponse**](ApiResponseListGetConcertDatesResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json;charset=UTF-8

<a name="getConcertSeats"></a>
# **getConcertSeats**
> ApiResponseListGetConcertSeatsResponse getConcertSeats(concertId, date)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConcertControllerApi;


ConcertControllerApi apiInstance = new ConcertControllerApi();
Long concertId = 789L; // Long | 
Long date = 789L; // Long | 
try {
    ApiResponseListGetConcertSeatsResponse result = apiInstance.getConcertSeats(concertId, date);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConcertControllerApi#getConcertSeats");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **concertId** | **Long**|  |
 **date** | **Long**|  |

### Return type

[**ApiResponseListGetConcertSeatsResponse**](ApiResponseListGetConcertSeatsResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json;charset=UTF-8

<a name="paySeat"></a>
# **paySeat**
> ApiResponseLong paySeat(body, userId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConcertControllerApi;


ConcertControllerApi apiInstance = new ConcertControllerApi();
PaySeatRequest body = new PaySeatRequest(); // PaySeatRequest | 
Long userId = 789L; // Long | 
try {
    ApiResponseLong result = apiInstance.paySeat(body, userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConcertControllerApi#paySeat");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**PaySeatRequest**](PaySeatRequest.md)|  |
 **userId** | **Long**|  |

### Return type

[**ApiResponseLong**](../ApiResponseLong.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json;charset=UTF-8
 - **Accept**: application/json;charset=UTF-8

<a name="reserveSeat"></a>
# **reserveSeat**
> ApiResponseLong reserveSeat(body, userId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.ConcertControllerApi;


ConcertControllerApi apiInstance = new ConcertControllerApi();
ReserveSeatRequest body = new ReserveSeatRequest(); // ReserveSeatRequest | 
Long userId = 789L; // Long | 
try {
    ApiResponseLong result = apiInstance.reserveSeat(body, userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConcertControllerApi#reserveSeat");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**ReserveSeatRequest**](ReserveSeatRequest.md)|  |
 **userId** | **Long**|  |

### Return type

[**ApiResponseLong**](../ApiResponseLong.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json;charset=UTF-8
 - **Accept**: application/json;charset=UTF-8

