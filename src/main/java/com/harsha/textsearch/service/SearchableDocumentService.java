package com.harsha.textsearch.service;

import com.harsha.textsearch.DocumentSearchRequest;
import com.harsha.textsearch.SearchableDocumentDTO;
import com.harsha.textsearch.UpdateDocumentRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SearchableDocumentService {
    Page<SearchableDocumentDTO> getAllDocuments();
    SearchableDocumentDTO createDocument(CreateDocumentRequest request);

    void deleteDocument(String id);
    Page<SearchableDocumentDTO> search(DocumentSearchRequest request);

    SearchableDocumentDTO updateDocument(UpdateDocumentRequest request);
}
