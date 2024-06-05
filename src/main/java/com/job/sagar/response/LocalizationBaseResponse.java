package com.job.sagar.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok. NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude. Include.NON_NULL)
public class LocalizationBaseResponse {
    @JsonProperty ("ERROR")
    private String error;
    @JsonProperty ("STATUS")
    private String status;
    @JsonProperty ("MESSAGE")
    private String message;
}
