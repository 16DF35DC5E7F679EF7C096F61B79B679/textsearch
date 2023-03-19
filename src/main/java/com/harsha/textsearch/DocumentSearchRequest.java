package com.harsha.textsearch;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;

@Data
@Builder
public class DocumentSearchRequest {
    private String keyword;
    private PageRequest pageRequest;
}
