package com.harsha.textsearch;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class SearchableDocumentDTO {
    private String id;
    private String name;
    private String text;
    private Date createdAt;
}
