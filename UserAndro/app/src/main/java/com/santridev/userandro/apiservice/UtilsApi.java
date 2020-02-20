package com.santridev.userandro.apiservice;

public class UtilsApi {
    private static final String BASE_URL_API = "http://192.168.43.228/UserAndro/public/api/";

    public static BaseApiService getAPIService(){
        return RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}
