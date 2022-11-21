package com.example.authmoduls.auth.repository.bookRepository;

import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface BookCustomRepository {

    List<BookPurchase> bookChartApi(int year) throws JSONException;

    List<BookDetailsData> bookData() throws JSONException;

    List<UserBookDetailsData> userBookData() throws JSONException;

    Page<BooksList> bookDetailsWithMonthAndYear(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws JSONException;

    List<BookTotalCountWithMonth> bookDataWithMonthAndYearAndTotalPrice(int month, int year) throws JSONException;

}
