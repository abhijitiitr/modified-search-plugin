/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.plugin.example;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentBuilderString;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.facet.Facets;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.internal.InternalSearchResponse;
import org.elasticsearch.search.SearchHitField;
import org.elasticsearch.common.inject.Inject;




import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.Object;
import java.util.Map;


import static org.elasticsearch.action.search.ShardSearchFailure.readShardSearchFailure;

/**
 * A response of a search request.
 */
public class ModifiedSearchResponse implements ToXContent {

    public SearchResponse response;
    // public ModifiedInternalSearchResponse modifiedInternalResponse;
    private ModifiedSearchResponse(){
    }
    @Inject
    public ModifiedSearchResponse(SearchResponse response){
      this.response = response;
       // this.modifiedInternalResponse = new ModifiedInternalSearchResponse(response.getHits(),response.getFacets(),response.getAggregations(),response.getSuggest());
    }
    static final class Fields {
        static final XContentBuilderString HITS = new XContentBuilderString("hits");
        static final XContentBuilderString TOTAL = new XContentBuilderString("total");
        static final XContentBuilderString MAX_SCORE = new XContentBuilderString("max_score");
    }
    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {

        builder.field(Fields.HITS);

        builder.startArray();
        for (SearchHit hit : response.getHits()) {
            Map<String, SearchHitField> fields = hit.getFields();
            builder.startObject();
            if (fields != null && !fields.isEmpty()) {
            // builder.startObject(Fields.FIELDS);

                for (SearchHitField field : fields.values()) {
                    if (field.values().isEmpty()) {
                        continue;
                    }
                    if (field.values().size() == 1) {
                        builder.field(field.name(), field.values().get(0));
                    } else {
                        builder.field(field.name());
                        builder.startArray();
                        for (Object value : field.values()) {
                            builder.value(value);
                        }
                        builder.endArray();
                    }
                }
            // builder.endObject();
            }
            builder.endObject();
        }
        builder.endArray();


       return builder;
   }


   @Override
   public String toString() {
       try {
           XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint();
           builder.startObject();
           toXContent(builder, EMPTY_PARAMS);
           builder.endObject();
           return builder.string();
       } catch (IOException e) {
           return "{ \"error\" : \"" + e.getMessage() + "\"}";
       }
   }
}
