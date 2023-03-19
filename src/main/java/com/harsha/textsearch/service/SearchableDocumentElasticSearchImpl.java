package com.harsha.textsearch.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harsha.textsearch.Constants;
import com.harsha.textsearch.DocumentSearchRequest;
import com.harsha.textsearch.SearchableDocumentDTO;
import com.harsha.textsearch.UpdateDocumentRequest;
import com.harsha.textsearch.search.SearchableDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.client.elc.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchableDocumentElasticSearchImpl implements SearchableDocumentService {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ElasticsearchOperations elasticsearchOperations;

    @Override
    public Page<SearchableDocumentDTO> getAllDocuments() {
        NativeQueryBuilder qb = new NativeQueryBuilder().withPageable(Pageable.ofSize(10));
        Query query = new NativeQuery(qb);
        SearchHits<SearchableDocument> searchResult =
                elasticsearchOperations
                        .search(query,
                                SearchableDocument.class,
                                IndexCoordinates.of(Constants.INDEX_NAME));
        long count = searchResult.getTotalHits();
        if (count == 0L) {
            return Page.empty();
        }
        List<SearchableDocument> documents = searchResult.getSearchHits().stream()
                .map(SearchHit::getContent).toList();
        return new PageImpl<>(documents.stream().map(mapToDTO).collect(Collectors.toList()),
                PageRequest.of(0, 10), count);
    }

    @Override
    public SearchableDocumentDTO createDocument(CreateDocumentRequest request) {
        validateDocumentCreateRequest(request);
        SearchableDocument document = new SearchableDocument();
        document.setCreatedAt(new Date());
        document.setName(request.getName());
        document.setId(UUID.randomUUID().toString());
        document.setText(request.getText());
        SearchableDocument savedDocument = createDocument(document);
        return mapToDTO.apply(savedDocument);
    }

    @Override
    public void deleteDocument(String id) {
        SearchableDocument document = elasticsearchOperations.get(id, SearchableDocument.class, IndexCoordinates.of(Constants.INDEX_NAME));
        if (document == null) throw new IllegalArgumentException("Invalid Id: "+ id);
        String deleteReturn = elasticsearchOperations
                .delete(id, IndexCoordinates.of(Constants.INDEX_NAME));
        System.out.println("[deleteDocument] id: " + id + " " + deleteReturn);
    }

    private static void validateDocumentCreateRequest(CreateDocumentRequest request) {
        if (request == null) throw new IllegalArgumentException("Invalid request");
        if (!StringUtils.hasLength(request.getName()))
            throw new IllegalArgumentException("Please pass a non-empty name");
        if (!StringUtils.hasLength(request.getText()))
            throw new IllegalArgumentException("Please pass a non-empty text");
    }

    private SearchableDocument createDocument(SearchableDocument document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(document.getId())
                .withObject(document).build();

        String id = elasticsearchOperations
                .index(indexQuery, IndexCoordinates.of(Constants.INDEX_NAME));
        document.setId(id);
        return document;
    }

    @Override
    public Page<SearchableDocumentDTO> search(DocumentSearchRequest request) {
        NativeQueryBuilder qb = new NativeQueryBuilder().withQuery(
                        QueryBuilders.matchQueryAsQuery("text", request.getKeyword(), Operator.Or, 1.0f)
                ).withPageable(request.getPageRequest())
                .withSort(Sort.by(Sort.Direction.DESC, "_score"));
        Query query = new NativeQuery(qb);
//        System.out.println("[search] Query: "+ gson.toJson(query));
        SearchHits<SearchableDocument> searchResult =
                elasticsearchOperations
                        .search(query,
                                SearchableDocument.class,
                                IndexCoordinates.of(Constants.INDEX_NAME));
        long count = searchResult.getTotalHits();
        if (count == 0L) {
            return Page.empty();
        }
        List<SearchableDocument> documents = searchResult.getSearchHits().stream()
                .map(SearchHit::getContent).toList();
        return new PageImpl<>(documents.stream().map(mapToDTO).collect(Collectors.toList()),
                request.getPageRequest(), count);
    }

    @Override
    public SearchableDocumentDTO updateDocument(UpdateDocumentRequest request) {
        if (request == null) throw new IllegalArgumentException("Invalid request");
        if (request.getId() == null || request.getId().isBlank())
            throw new IllegalArgumentException("Id can't be null");
        SearchableDocument document = elasticsearchOperations.get(request.getId(), SearchableDocument.class, IndexCoordinates.of(Constants.INDEX_NAME));
        if (document == null) throw new IllegalArgumentException("Invalid Id: " + request.getId());
        if (StringUtils.hasLength(request.getName()) && !request.getName().equals(document.getName())) {
            document.setName(request.getName());
        }
        if (StringUtils.hasLength(request.getText()) && !request.getText().equals(document.getText())) {
            document.setText(request.getText());
        }
        elasticsearchOperations.update(document);
        return mapToDTO.apply(document);
    }


    private static final Function<SearchableDocument, SearchableDocumentDTO> mapToDTO = (document -> {
        return SearchableDocumentDTO.builder()
                .createdAt(document.getCreatedAt())
                .id(document.getId())
                .name(document.getName())
                .text(document.getText())
                .build();
    });
}
