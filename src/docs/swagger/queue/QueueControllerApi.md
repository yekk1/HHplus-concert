# QueueControllerApi

All URIs are relative to *http://localhost:8080*

Method | HTTP request | Description
------------- | ------------- | -------------
[**checkQueue**](QueueControllerApi.md#checkQueue) | **GET** /queue/{userId} | 

<a name="checkQueue"></a>
# **checkQueue**
> ApiResponseCheckQueueResponse checkQueue(userId)



### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.QueueControllerApi;


QueueControllerApi apiInstance = new QueueControllerApi();
Long userId = 789L; // Long | 
try {
    ApiResponseCheckQueueResponse result = apiInstance.checkQueue(userId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling QueueControllerApi#checkQueue");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **userId** | **Long**|  |

### Return type

[**ApiResponseCheckQueueResponse**](ApiResponseCheckQueueResponse.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json;charset=UTF-8

