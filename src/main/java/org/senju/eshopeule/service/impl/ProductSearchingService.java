package org.senju.eshopeule.service.impl;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.MultiBucketBase;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.senju.eshopeule.dto.ProductSimpleDTO;
import org.senju.eshopeule.dto.response.ProductSearchResultResponse;
import org.senju.eshopeule.exceptions.PagingException;
import org.senju.eshopeule.mappers.ProductSearchMapper;
import org.senju.eshopeule.model.product.ProductESDoc;
import org.senju.eshopeule.service.SearchingService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.Aggregation;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregation;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchingService implements SearchingService {

    private final ProductSearchMapper mapper;
    private final ElasticsearchOperations esOperations;
    private static final Map<String, String> sortablePropertiesMap;

    static {
        sortablePropertiesMap = Map.of(
                "price", ProductElasticSearchField.PRICE_FIELD,
                "last_updated", ProductElasticSearchField.LAST_MODIFIED_FIELD
        );
    }

    @Override
    public ProductSearchResultResponse search(String keyword, String brandName, List<String> categoryNames,
                                            Double minPrice, Double maxPrice, Pageable pageRequest) {
        NativeQueryBuilder nativeQueryBuilder = NativeQuery.builder()
                .withAggregation("brands", co.elastic.clients.elasticsearch._types.aggregations.Aggregation.of(a -> a
                        .terms(te -> te.field(ProductElasticSearchField.BRAND_FIELD + ".keyword"))))
                .withAggregation("categories", co.elastic.clients.elasticsearch._types.aggregations.Aggregation.of(a -> a
                        .terms(te -> te.field(ProductElasticSearchField.CATEGORIES_FIELD))))
                .withQuery(q -> q
                        .bool(b -> b
                                .must(mu -> mu
                                        .match(ma -> ma
                                                .field(ProductElasticSearchField.NAME_FIELD)
                                                .query(keyword)
                                                .fuzziness(Fuzziness.TWO.asString())
                                                .prefixLength(2)
                                                .maxExpansions(50)
                                        )
                                )
                        )
                )
                .withFilter(f -> f
                        .bool(b -> {
                            extractedList(brandName != null ? List.of(brandName) : new ArrayList<>(), ProductElasticSearchField.BRAND_FIELD, b);
                            extractedList(categoryNames, ProductElasticSearchField.CATEGORIES_FIELD, b);
                            extractedRange(minPrice, maxPrice, ProductElasticSearchField.PRICE_FIELD, b);
                            return b;
                        })
                )
                .withPageable(PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize()));

        this.mappingSortField(pageRequest.getSort(), nativeQueryBuilder);

        SearchHits<ProductESDoc> searchHitRes = esOperations.search(nativeQueryBuilder.build(), ProductESDoc.class);
        SearchPage<ProductESDoc> searchResPage = SearchHitSupport.searchPageFor(searchHitRes, nativeQueryBuilder.getPageable());
        List<ProductSimpleDTO> productResList = searchResPage.getSearchHits().stream()
                .map(sh -> mapper.convertToDTO(sh.getContent()))
                .toList();

        return new ProductSearchResultResponse(
                searchResPage.getTotalElements(),
                searchResPage.getTotalPages(),
                searchResPage.getNumber() + 1,
                searchResPage.getSize(),
                searchResPage.isLast(),
                productResList,
                getAggregation(searchHitRes)
        );
    }

    private void extractedList(List<String> inputSearchList, String documentField, BoolQuery.Builder b) {
        if (inputSearchList != null && !inputSearchList.isEmpty()) {
            inputSearchList.forEach(inputStr -> b
                    .must(m -> m
                            .term(t -> t
                                    .field(documentField)
                                    .value(inputStr)
                                    .caseInsensitive(true)
                            )
                    )
            );
        }
    }

    private void extractedRange(Number min, Number max, String documentField, BoolQuery.Builder b) {
        if (min != null || max != null) {
            b.must(m -> m
                    .range(r -> r
                            .field(documentField)
                            .from(min != null ? min.toString() : null)
                            .to(max != null ? max.toString() : null)
                    ));
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Map<String, Long>> getAggregation(SearchHits<ProductESDoc> searchResult) {
        if (searchResult.hasAggregations() && searchResult.getAggregations() != null) {
            List<Aggregation> aggregations = ((List<ElasticsearchAggregation>) searchResult.getAggregations().aggregations())
                    .stream()
                    .map(ElasticsearchAggregation::aggregation)
                    .toList();
            return aggregations.stream().collect(Collectors.toMap(
                    Aggregation::getName,
                    agg -> {
                        StringTermsAggregate stringTermsAggregate = (StringTermsAggregate) agg.getAggregate()._get();
                        List<StringTermsBucket> stringTermsBuckets = (List<StringTermsBucket>) stringTermsAggregate.buckets()._get();
                        return stringTermsBuckets.stream().collect(Collectors.toMap(
                                bucket -> bucket.key()._get().toString(),
                                MultiBucketBase::docCount
                        ));
                    }
            ));
        }
        return null;
    }


    private void mappingSortField(Sort sort, NativeQueryBuilder builder) {
        sort.forEach(
                order -> {
                    builder.withSort(sb -> sb
                            .field(fsb -> {
                                if (sortablePropertiesMap.containsKey(order.getProperty())) {
                                    return fsb.field(sortablePropertiesMap.get(order.getProperty()))
                                            .order(order.getDirection().equals(Sort.Direction.ASC) ? SortOrder.Asc : SortOrder.Desc);
                                } else throw new PagingException("Unsupported sort field: " + order.getProperty());
                            })
                    );
                }
        );
    }

    private static class ProductElasticSearchField {
        private static final String NAME_FIELD = "name";
        private static final String CATEGORIES_FIELD = "categories";
        private static final String BRAND_FIELD = "brand";
        private static final String PRICE_FIELD = "price";
        private static final String LAST_MODIFIED_FIELD = "lastModifiedOn";
    }
}
