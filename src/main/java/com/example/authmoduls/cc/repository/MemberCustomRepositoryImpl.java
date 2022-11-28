package com.example.authmoduls.cc.repository;

import com.example.authmoduls.auth.decorator.UserFilter;
import com.example.authmoduls.auth.enums.UserSortBy;
import com.example.authmoduls.cc.decorator.MemberResponse;
import com.example.authmoduls.common.decorator.CountQueryResult;
import com.example.authmoduls.common.decorator.CustomAggregationOperation;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

public class MemberCustomRepositoryImpl implements MemberCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<MemberResponse> findAllUserByFilterAndSortAndPage(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException {

        List<AggregationOperation> operations = userFilterAggregation(filter, sort, pagination, true);


        //created Aggregation operation
        Aggregation aggregation = newAggregation(operations);

        List<MemberResponse> memberResponses = mongoTemplate.aggregate(aggregation, "users", MemberResponse.class).getMappedResults();

        // Find Count
        List<AggregationOperation> operationForCount = userFilterAggregation(filter, sort, pagination, false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(MemberResponse.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount, "users", CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return memberResponses;
    }

    private List<AggregationOperation> userFilterAggregation(UserFilter filter, FilterSortRequest.SortRequest<UserSortBy> sort, PageRequest pagination, boolean addPage) {
        List<AggregationOperation> operations = new ArrayList<>();

        operations.add(match(getCriteria(filter, operations)));

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

    private Criteria getCriteria(UserFilter userFilter, List<AggregationOperation> operations) {
        Criteria criteria = new Criteria();
        operations.add(new CustomAggregationOperation(new Document("$addFields", new Document("search", new Document("$concat", List.of(new Document("$ifNull", Arrays.asList("|@|", new Document("$ifNull", Arrays.asList("$email", "")), "|@|", new Document("$ifNull", Arrays.asList("$lastName", "")), "|@|", new Document("$ifNull", Arrays.asList("$middleName", "")), "|@|", new Document("$ifNull", Arrays.asList("$firstName", "")), "|@|", new Document("$ifNull", Arrays.asList("$address.address1", "")), "|@|", new Document("$ifNull", Arrays.asList("$address.city", "")), "|@|", new Document("$ifNull", Arrays.asList("$address.state", "")), "|@|", new Document("$ifNull", Arrays.asList("$address.zipCode", ""))))))))));

        if (!StringUtils.isEmpty(userFilter.getSearch())) {
            userFilter.setSearch(userFilter.getSearch().replaceAll("\\|@\\|", ""));
            userFilter.setSearch(userFilter.getSearch().replaceAll("\\|@@\\|", ""));
            criteria = criteria.orOperator(Criteria.where("search").regex(".*" + userFilter.getSearch() + ".*", "i"));
        }
        if (!StringUtils.isEmpty(userFilter.getId())) {
            criteria = criteria.and("_id").in(userFilter.getId());
        }
        if (userFilter.getRole() != null) {
            criteria = criteria.and("plan").is(userFilter.getPlan());
        }
        criteria = criteria.and("softDelete").is(false);
        return criteria;
    }
}
