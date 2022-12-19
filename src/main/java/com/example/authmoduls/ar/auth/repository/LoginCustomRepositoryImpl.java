package com.example.authmoduls.ar.auth.repository;

import com.example.authmoduls.ar.auth.decorator.LoginAddRequest;
import com.example.authmoduls.ar.auth.decorator.LoginFilter;
import com.example.authmoduls.ar.auth.decorator.LoginSortBy;
import com.example.authmoduls.ar.auth.model.Login;
import com.example.authmoduls.common.decorator.CountQueryResult;
import com.example.authmoduls.common.decorator.CustomAggregationOperation;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
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

public class LoginCustomRepositoryImpl implements LoginCustomRepository{
    @Autowired
    MongoTemplate mongoTemplate;
    @Override
    public Page<Login> findAllUserByFilterAndSortAndPage(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination) {
        List<AggregationOperation> operations = filterAggregation(loginFilter, sort, pagination, true);
        //created Aggregation operation
        Aggregation aggregation = newAggregation(operations);
        List<Login> logins = mongoTemplate.aggregate(aggregation, "userData", Login.class).getMappedResults();
        // Find Count
        List<AggregationOperation> operationForCount = filterAggregation(loginFilter, sort, pagination, false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(LoginAddRequest.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount, "userData", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().isEmpty() ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                logins,
                pagination,
                () -> count);
    }

    private List<AggregationOperation> filterAggregation(LoginFilter loginFilter, FilterSortRequest.SortRequest<LoginSortBy> sort, PageRequest pagination, boolean addPage) {
        List<AggregationOperation> operations = new ArrayList<>();
        operations.add(match(getCriteria(loginFilter, operations)));
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

    private Criteria getCriteria(LoginFilter loginFilter, List<AggregationOperation> operations) {
        Criteria criteria = getSearchCriteria(loginFilter.getSearch(),operations);
        if (!CollectionUtils.isEmpty(loginFilter.getId())) {
            criteria = criteria.and("_id").is(loginFilter.getId());
        }
        if (loginFilter.getGender() != null) {
            criteria = criteria.and("gender").is(loginFilter.getGender());
        }
        if (loginFilter.getAccesss() !=null){
            criteria = criteria.and("accesss").is(loginFilter.getAccesss());
        }
        criteria = criteria.and("softDelete").is(loginFilter.isSoftDelete());
        return criteria;
    }

    private Criteria getSearchCriteria(String search, List<AggregationOperation> operations) {

        Criteria criteria = new Criteria();
        operations.add(
                new CustomAggregationOperation(
                        new Document("$addFields",
                                new Document("search",
                                        new Document("$concat", Arrays.asList(
                                                new Document("$ifNull", Arrays.asList("$firstName", "")),
                                                "|@|",new Document("$ifNull", Arrays.asList("$middleName", "")),
                                                "|@|",new Document("$ifNull", Arrays.asList("$lastName", "")),
                                                "|@|",new Document("$ifNull",Arrays.asList("$email", ""))
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
