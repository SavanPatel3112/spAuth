package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.RecipeFilter;
import com.example.authmoduls.ar.auth.decorator.RecipeSortBy;
import com.example.authmoduls.ar.auth.model.RecipeModel;
import com.example.authmoduls.common.decorator.CountQueryResult;
import com.example.authmoduls.common.decorator.CustomAggregationOperation;
import com.example.authmoduls.common.decorator.FilterSortRequest;
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
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Repository
public class RecipeCustomRepositoryImp implements RecipeCustomRepository {

    private final MongoTemplate mongoTemplate;

    public RecipeCustomRepositoryImp(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    /*@Override
    public Page<RecipeModel> findAllRecipeByFilterAndSortAndPage(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination) {

        List<AggregationOperation> operations = filterAggregation(recipeFilter, sort, pagination, true);
        //created Aggregation operation
        Aggregation aggregation = newAggregation(operations);
        List<RecipeModel> recipeModels = mongoTemplate.aggregate(aggregation, "recipe", RecipeModel.class).getMappedResults();
        // Find Count
        List<AggregationOperation> operationForCount = filterAggregation(recipeFilter, sort, pagination, false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(RecipeModel.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount, "recipe", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().isEmpty() ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                recipeModels,
                pagination,
                () -> count);

    }

    private List<AggregationOperation> filterAggregation(RecipeFilter recipeFilter, FilterSortRequest.SortRequest<RecipeSortBy> sort, PageRequest pagination, boolean addPage) {

        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(getCriteria(recipeFilter, operations)));
        if (addPage) {
            //sorting
            if (sort != null && sort.getSortBy() != null && sort.getOrderBy() != null) {
                operations.add(new SortOperation(Sort.by(sort.getOrderBy(), sort.getSortBy().getValue())));
            }
            if (pagination != null) {
                operations.add(skip(pagination.getOffset()));
                operations.add(limit(pagination.getPageSize()));
            }
        }
        return operations;

    }

    private Criteria getCriteria(RecipeFilter recipeFilter, List<AggregationOperation> operations) {
        Criteria criteria = searchCriteria(recipeFilter.getSearch(),operations);
        if (!CollectionUtils.isEmpty(recipeFilter.getId())) {
            criteria = criteria.and("_id").is(recipeFilter.getId());
        }
        if (recipeFilter.getItemName() != null) {
            criteria = criteria.and("itemName").is(recipeFilter.getItemName());
        }
        if (recipeFilter.getItemDescription() !=null){
            criteria = criteria.and("itemDescription").is(recipeFilter.getItemDescription());
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
*/

}
