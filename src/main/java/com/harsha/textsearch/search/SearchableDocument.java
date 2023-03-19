package com.harsha.textsearch.search;


import com.harsha.textsearch.Constants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.stereotype.Indexed;

import java.util.Date;

//@Entity
@Indexed
@Document(indexName = Constants.INDEX_NAME)
@Data
public class SearchableDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "text", analyzer = "stop")
    private String text;

    @Field(type = FieldType.Date, name = "created_at")
    private Date createdAt;


}
