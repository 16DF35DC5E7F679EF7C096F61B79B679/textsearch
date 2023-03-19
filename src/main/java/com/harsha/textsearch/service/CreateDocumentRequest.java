package com.harsha.textsearch.service;

import lombok.Data;

@Data
public class CreateDocumentRequest {
    private String name;
    private String text;
}
