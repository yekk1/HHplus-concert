# PointControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**chargePoint**](PointControllerApi.md#chargePoint) | **POST** /point/{userId}/charge | 
[**getPoint**](PointControllerApi.md#getPoint) | **GET** /point/{userId} | 

<a name="chargePoint"></a>
# **chargePoint**
> ApiResponseLong chargePoint(body, userId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.PointControllerApi;


PointControllerApi apiInstance = new PointControllerApi();
Long body = 56L; // Long | 
Long userId = 789L; // Long | 
try {
    ApiResponseLong result = apiInstance.chargePoint(body, userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointControllerApi#chargePoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**Long**](Long.md)|  |
 **userId** | **Long**|  |

### Return type

[**ApiResponseLong**](../ApiResponseLong.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json;charset=UTF-8
 - **Accept**: application/json;charset=UTF-8

<a name="getPoint"></a>
# **getPoint**
> ApiResponseInteger getPoint(userId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.PointControllerApi;


PointControllerApi apiInstance = new PointControllerApi();
Long userId = 789L; // Long | 
try {
    ApiResponseInteger result = apiInstance.getPoint(userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointControllerApi#getPoint");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **Long**|  |

### Return type

[**ApiResponseInteger**](../ApiResponseInteger.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json;charset=UTF-8

