package app.quantun.summary.model.contract.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record TableIndexContent(List<IndexContent> indexContents) {
}
