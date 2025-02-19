package app.quantun.summary.model.contract.dto;


import app.quantun.summary.model.entity.Script;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.TreeSet;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PodCast {


    private String title;
    private Long duration;


    private TreeSet<Script> scripts;


}
