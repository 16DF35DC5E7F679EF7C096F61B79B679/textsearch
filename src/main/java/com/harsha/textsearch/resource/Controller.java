package com.harsha.textsearch.resource;

import com.harsha.textsearch.DocumentSearchRequest;
import com.harsha.textsearch.SearchableDocumentDTO;
import com.harsha.textsearch.UpdateDocumentRequest;
import com.harsha.textsearch.service.CreateDocumentRequest;
import com.harsha.textsearch.service.SearchableDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/document")
@RequiredArgsConstructor
public class Controller {
    private final SearchableDocumentService service;
    @GetMapping("")
    public ResponseEntity<GenericResponse<Page<SearchableDocumentDTO>>> getAllDocuments() {
        return new ResponseEntity<>(new GenericResponse<>("List of documents", service.getAllDocuments()), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<GenericResponse<SearchableDocumentDTO>> createDocument(@RequestBody CreateDocumentRequest request) {
        return new ResponseEntity<>(new GenericResponse<>("Created the document", service.createDocument(request)), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<GenericResponse<Page<SearchableDocumentDTO>>> searchText(@RequestParam(name = "keyword") String keyword) {
        var request = DocumentSearchRequest.builder()
                .keyword(keyword)
                .pageRequest(PageRequest.of(0, 10))
                .build();
        return new ResponseEntity<>(new GenericResponse<>("Search result", service.search(request)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> removeDocument(@PathVariable("id") String id) {
        service.deleteDocument(id);
        return new ResponseEntity<>(new GenericResponse<>("Deleted the document", null), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GenericResponse<SearchableDocumentDTO>> updateDocument(@PathVariable("id") String id,
                                                                @RequestBody UpdateDocumentRequest request) {
        request.setId(id);
        return new ResponseEntity<>(new GenericResponse<>("Updated the document", service.updateDocument(request)), HttpStatus.OK);
    }

}
