package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.RecipeFilter;
import com.example.authmoduls.ar.auth.decorator.RecipeSortBy;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.common.constant.MessageConstant;
import com.example.authmoduls.common.decorator.CountQueryResult;
import com.example.authmoduls.common.decorator.CustomAggregationOperation;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class RecipeCustomRepositoryImpl implements RecipeCustomRepository {

    private final MongoTemplate mongoTemplate;

    public RecipeCustomRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    @Override
    public Page<RecipeModel> getAllRecipesByFilterAndSort(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination) {
        List<AggregationOperation> operations = filterAggregation(recipeFilter,sort,pagination,true);
        Aggregation aggregation = newAggregation(operations);
        List<RecipeModel> recipeResponse = mongoTemplate.aggregate(aggregation,"recipe",RecipeModel.class).getMappedResults();
        List<AggregationOperation> operationList = filterAggregation(recipeFilter,sort,pagination,false);
        operationList.add(group().count().as("count"));
        operations.add(project("count"));
        Aggregation aggregation1 = newAggregation(RecipeModel.class,operationList);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregation1,"recipe", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().isEmpty() ? 0 :countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
               recipeResponse,
               pagination,
               () ->count);
    }

    private List<AggregationOperation> filterAggregation(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination, boolean addPage) {
        List<AggregationOperation> operationList = new ArrayList<>();
        operationList.add(match(getCriteria(recipeFilter, operationList)));
        if (addPage){
            if (sort !=null && sort.getSortBy() !=null && sort.getOrderBy() !=null){
                operationList.add(new SortOperation(Sort.by(sort.getOrderBy(),sort.getSortBy().getValue())));
            }if (pagination!=null){
                operationList.add(skip(pagination.getOffset()));
                operationList.add(limit(pagination.getPageSize()));
            }
        }
        return operationList;
    }

    private Criteria getCriteria(RecipeFilter recipeFilter, List<AggregationOperation> operations) {
        Criteria criteria = searchCriteria(recipeFilter.getSearch(),operations);
        if (!CollectionUtils.isEmpty(recipeFilter.getId())) {
            criteria = criteria.and("_id").is(recipeFilter.getId());
        }
        criteria = criteria.and("softDelete").is(recipeFilter.isSoftDelete());
        return criteria;
    }

    private Criteria searchCriteria(String search, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(
                new CustomAggregationOperation(
                        new Document("$addFields",
                                new Document("search",
                                        new Document("$concat", Arrays.asList(
                                                new Document("$ifNull", Arrays.asList("$itemName", "")),
                                                "|@|",new Document("$ifNull", Arrays.asList("$itemDescription", ""))
                                        )
                                        )
                                )
                        )
                )
        );
        if (!StringUtils.isEmpty(search)) {
            search = search.replace("\\|@\\|","");
            search = search.replace("\\|@@\\|","");
            criteria = criteria.orOperator(
                    Criteria.where("search").regex(".*" + search + ".*", "i")
            );
        }
        return criteria;
    }

}
