package com.github.testairbnd.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lodging {

    @SerializedName("search_results")
    private List<Result> result;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
}
