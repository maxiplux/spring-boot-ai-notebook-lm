package app.quantun.summary.model.contract.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record IndexContent(String title, String index, Integer PageStart, Integer PageEnd) {
}
