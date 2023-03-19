package com.harsha.textsearch;

import lombok.Data;

@Data
public class UpdateDocumentRequest {
    private String id;
    private String name;
    private String text;
}
