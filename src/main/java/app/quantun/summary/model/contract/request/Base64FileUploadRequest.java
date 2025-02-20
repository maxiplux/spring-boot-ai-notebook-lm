package app.quantun.summary.model.contract.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public  class Base64FileUploadRequest {
    private String base64File;
    private String fileName;
}

