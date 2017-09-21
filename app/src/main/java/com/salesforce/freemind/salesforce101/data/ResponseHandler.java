package com.salesforce.freemind.salesforce101.data;

/**
 * Created by anant on 2017-09-20.
 */

public class ResponseHandler {

    private int responseCode;
    private String responseBody;

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public int getResponseCode() {

        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseData(int responseCode ,String responseBody){

        this.responseCode = responseCode;
        this.responseBody = responseBody;

    }

}
