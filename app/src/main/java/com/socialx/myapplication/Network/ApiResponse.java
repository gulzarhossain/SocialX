package com.socialx.myapplication.Network;

public interface ApiResponse {
    void OnResponse(String response,int apiRequest);
    void OnError(String errorResponse,int apiRequest);
}
