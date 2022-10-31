package com.example.authmoduls.auth.service.bookService;

import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.enums.Role;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface BookService {
    BookResponse addOrUpdateBook(BookAddRequest bookAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException;
    void bookPurchaseLog(String id, String userId) throws InvocationTargetException, IllegalAccessException;
    BookPurchaseDetail bookChartApi(int year) throws JSONException;
    void bookSaleLog(String bookId, String userId );
    Workbook getBookData() throws JSONException, InvocationTargetException, IllegalAccessException, IOException;
    Workbook getBookDataWithMonthAndYear(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException, JSONException;
    Workbook getUserBookData() throws JSONException, InvocationTargetException, IllegalAccessException, IOException;
    Page<BooksList> getBookListByPagination(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException, JSONException;
    List<BookTotalCountWithMonth> bookDataWithMonthAndYearAndTotalPrice(int month, int year) throws JSONException;
}

