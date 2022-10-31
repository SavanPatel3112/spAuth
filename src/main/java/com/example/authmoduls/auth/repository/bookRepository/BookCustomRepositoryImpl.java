package com.example.authmoduls.auth.repository.bookRepository;
import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.common.decorator.CountQueryResult;
import com.example.authmoduls.common.decorator.CustomAggregationOperation;
import com.example.authmoduls.common.decorator.FileReader;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.bson.Document;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.ArrayList;
import java.util.List;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
public class BookCustomRepositoryImpl implements BookCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;
    private List<AggregationOperation> getBookDetails(int year) throws JSONException {
        List<AggregationOperation> operations = new ArrayList<>();
        String fileName = FileReader.loadFile("aggregation/bookPurchaseChart.json");
        JSONObject jsonObject = new JSONObject(fileName);
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "extractMonAndYear", Object.class))));
        operations.add(match(new Criteria("year").is(year)));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupByMonthAndBookName", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupByBookName", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unwindBookData", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupByMonth", Object.class))));
        return operations;
    }
    private List<AggregationOperation> getBookData() throws JSONException {
        List<AggregationOperation> operations = new ArrayList<>();
        Criteria criteria = new Criteria();
        String fileName = FileReader.loadFile("aggregation/bookDataByExcel.json");
        JSONObject jsonObject = new JSONObject(fileName);
        criteria = criteria.and("softDelete").is(false);
        operations.add(match(criteria));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "lookup", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unwind", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "setTheStudentDate", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unsetTheStudent", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "group", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "set", Object.class))));
        return operations;
    }
    private List<AggregationOperation> getUserBookData() throws JSONException {
        List<AggregationOperation> operations = new ArrayList<>();
        Criteria criteria = new Criteria();
        String fileName = FileReader.loadFile("aggregation/userBookDataByExcel.json");
        JSONObject jsonObject = new JSONObject(fileName);
        criteria = criteria.and("softDelete").is(false);
        operations.add(match(criteria));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "lookup", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unwindStudentDetails", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "setFullNameInStudentDetails", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unsetTheStudentDetails", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupOnFullNameAndBookName", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupOnFullName", Object.class))));
        return operations;
    }
    private List<AggregationOperation> getBookDetailsWithMontAndYear(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination,boolean addPage) throws JSONException {
        List<AggregationOperation> operations = new ArrayList<>();
        String fileName = FileReader.loadFile("aggregation/getDataWithMonthAndYear.json");
        JSONObject jsonObject = new JSONObject(fileName);
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "setMonthAndYear", Object.class))));
        operations.add(match(Criteria.where("year").is(bookFilter.getYear())));
        operations.add(match(Criteria.where("month").is(bookFilter.getMonth())));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "lookup", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unwindTheStudentData", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupOnUserId", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "setUserId", Object.class))));
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
    public List<BookPurchase> bookChartApi(int year) throws JSONException {
        List<AggregationOperation> operations = getBookDetails(year);
        Aggregation aggregation = newAggregation(operations);
        return mongoTemplate.aggregate(aggregation, "bookPurchaseLog", BookPurchase.class).getMappedResults();
    }
    public List<BookDetailsData> bookData() throws JSONException {
        List<AggregationOperation> operations = getBookData();
        Aggregation aggregation =  newAggregation(operations);
        return mongoTemplate.aggregate(aggregation, "bookPurchaseLog", BookDetailsData.class).getMappedResults();
    }
    public List<UserBookDetailsData> userBookData() throws JSONException {
        List<AggregationOperation> operations = getUserBookData();
        Aggregation aggregation =  newAggregation(operations);
        return mongoTemplate.aggregate(aggregation, "bookPurchaseLog", UserBookDetailsData.class).getMappedResults();
    }
    public Page<BooksList> bookDetailsWithMonthAndYear(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws JSONException {
        List<AggregationOperation> operations = getBookDetailsWithMontAndYear(bookFilter,sort,pagination,true);
        Aggregation aggregation = newAggregation(operations);
        List<BooksList> booksLists = mongoTemplate.aggregate(aggregation, "bookPurchaseLog", BooksList.class).getMappedResults();
        List<AggregationOperation> operationForCount =getBookDetailsWithMontAndYear(bookFilter,sort,pagination,false);
        operationForCount.add(group().count().as("count"));
        operationForCount.add(project("count"));
        Aggregation aggregationCount = newAggregation(BooksList.class, operationForCount);
        AggregationResults<CountQueryResult> countQueryResults = mongoTemplate.aggregate(aggregationCount , BooksList.class, CountQueryResult.class);
        long count = countQueryResults.getMappedResults().size() == 0 ? 0 : countQueryResults.getMappedResults().get(0).getCount();
        return PageableExecutionUtils.getPage(
                booksLists,
                pagination,
                () -> count);
    }
    public List<BookTotalCountWithMonth> bookDataWithMonthAndYearAndTotalPrice(int month, int year) throws JSONException {
        List<AggregationOperation> operations = getBookDataWithMonthAndYearAndTotalPrice(month,year);
        Aggregation aggregation = newAggregation(operations);
        return mongoTemplate.aggregate(aggregation, "bookPurchaseLog", BookTotalCountWithMonth.class).getMappedResults();
    }
    private List<AggregationOperation> getBookDataWithMonthAndYearAndTotalPrice(int month, int year) throws JSONException {
        List<AggregationOperation> operations = new ArrayList<>();
        String fileName = FileReader.loadFile("aggregation/bookDataWithMonthAndYearAndTotalPrice.json");
        JSONObject jsonObject = new JSONObject(fileName);
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "set", Object.class))));
        operations.add(match(new Criteria("year").is(year)));
        operations.add(match(new Criteria("month").is(month)));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "lookup", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "unwind", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "group", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "groupOnMonth", Object.class))));
        operations.add(new CustomAggregationOperation(Document.parse(CustomAggregationOperation.getJson(jsonObject, "setMonth", Object.class))));
        return operations;
    }
}
