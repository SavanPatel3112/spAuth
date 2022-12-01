package com.example.authmoduls.service;

import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.auth.model.Book;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.repository.bookRepository.BookPurchaseLogRepository;
import com.example.authmoduls.auth.repository.bookRepository.BookRepository;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.bookService.BookService;
import com.example.authmoduls.auth.service.bookService.BookServiceImpl;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import javax.annotation.meta.When;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.mockito.Mockito.*;
@AutoConfigureMockMvc
@Slf4j
class BookServiceImplTest {

    private static final String id =  "633aa3e981b01b4fcc7fbcd1";
    private static final String userId =  "62eb607d9c735854c58f83dd";
    private static final String bookName =  "SpringBoot";
    private static final String authorName =  "Savan";
    private static final String price =  "30";
    private static final double discount =  5;
    private static final double balance = 855;
    private static final int year = 2021;
    private static final int count = 5;
    private static final int totalCount = 5;
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean = mock(NullAwareBeanUtilsBean.class);
    private final ModelMapper modelMapper = mock(ModelMapper.class);
    private final UserService userService = mock(UserService.class);
    private final BookPurchaseLogRepository bookPurchaseLogRepository = mock(BookPurchaseLogRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final AdminConfiguration adminConfiguration = mock(AdminConfiguration.class);
    private final Utils utils = mock(Utils.class);
    private final BookService bookService = new BookServiceImpl(bookRepository,nullAwareBeanUtilsBean,modelMapper,userService,bookPurchaseLogRepository,userRepository,adminConfiguration,utils);

    @Test
    void testAddOrUpdateBook() throws InvocationTargetException, IllegalAccessException {
        //given
        var bookAddRequest = BookAddRequest.builder().id(id).bookName(bookName).authorName(authorName).price(price).discount(discount).build();
        var bookResponse = BookResponse.builder().id(id).bookName(bookName).authorName(authorName).price(price).discount(discount).build();
        var book = Book.builder().id(id).bookName(bookName).authorName(authorName).price(Double.parseDouble(price)).discount(discount).date(new Date()).role(Role.ADMIN).build();
        when(bookRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(book));
        nullAwareBeanUtilsBean.copyProperties(book, bookAddRequest);
        when(modelMapper.map(book, BookResponse.class)).thenReturn(bookResponse);
        //when
        bookService.addOrUpdateBook(bookAddRequest, id, Role.ADMIN);
        //then
        Assertions.assertEquals(bookResponse, bookService.addOrUpdateBook(bookAddRequest, id, Role.ADMIN));
    }

    @Test
    void testBookPurchaseLog() throws InvocationTargetException, IllegalAccessException {
        //given
        var userModel = UserModel.builder().id(id).balance(456.56).build();
        var bookPurchaseLog = List.of(BookPurchaseLog.builder().bookId(id).bookName(bookName).userId(userId).balance(String.valueOf(balance)).price(Double.parseDouble(price)).softDelete(false).build());
        var book = Book.builder().id(id).bookName(bookName).authorName(authorName).bookPurchaseLogs(bookPurchaseLog).price(Double.parseDouble(price)).build();
        var bookPurchaseLogs = BookPurchaseLog.builder().bookId(id).bookName(bookName).userId(userId).balance(String.valueOf(balance)).price(Double.parseDouble(price)).softDelete(false).build();
        //when
        when(userRepository.findByIdAndSoftDeleteIsFalse(userId)).thenReturn(Optional.ofNullable(userModel));
        when(bookRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(book));
        bookService.bookPurchaseLog(id,userId);
        //then
        verify(bookRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
     /*   verify(userRepository,times(1)).findByIdAndSoftDeleteIsFalse(userId);
        verify(bookPurchaseLogRepository,times(1)).save(bookPurchaseLogs);*/
    }

    @Test
    void testBookChartApi() throws JSONException, InvocationTargetException, IllegalAccessException {
        //given
        HashMap<String, Integer> month = new LinkedHashMap<>();
        Set<String> title = new LinkedHashSet<>();
        month.put("JAN", 1);
        month.put("FEB", 2);
        month.put("MAR", 3);
        month.put("APR", 4);
        month.put("MAY", 5);
        month.put("JUNE", 6);
        month.put("JUL", 7);
        month.put("AUG", 8);
        month.put("SEP", 9);
        month.put("OCT", 10);
        month.put("NOV", 11);
        month.put("DEC", 12);
        HashMap<String, String> bookNameHashMap = new LinkedHashMap<>();
        bookNameHashMap.put("1", "SpringBoot");
        bookNameHashMap.put("2", "Java");
        bookNameHashMap.put("3", "The Exiled");
        bookNameHashMap.put("4", "The Complete Harry Potter");
        bookNameHashMap.put("5", "Antonina Or The Fall Of Rome");
        var bookData = List.of(BookData.builder().bookName(bookName).count(count).build());
        var bookPurchase = List.of(BookPurchase.builder().bookData(bookData).totalCount(totalCount).build());
        var bookPurchaseDetail = BookPurchaseDetail.builder().bookDataResponse(bookPurchase).title(title).totalCount(totalCount).build();
        //when
        when(bookRepository.bookChartApi(year)).thenReturn(bookPurchase);
        bookService.bookChartApi(year);
        //then
        Assertions.assertEquals(bookPurchaseDetail,bookService.bookChartApi(year));
    }

    @Test
    void testBookSaleLog(){
        //given
        var bookPurchaseLog = BookPurchaseLog.builder().bookName(bookName).balance(String.valueOf(balance)).bookId(id).userId(userId).resale(true).price(Double.parseDouble(price)).build();
        var userModel = UserModel.builder().id(userId).balance(balance).build();
        var book = Book.builder().id(id).authorName(authorName).bookName(bookName).price(Double.parseDouble(price)).discount(discount).role(Role.STUDENT).softDelete(false).build();
        //when
        when(bookPurchaseLogRepository.findFirstByBookIdAndUserIdAndSoftDeleteIsFalse(id, userId)).thenReturn(Optional.ofNullable(bookPurchaseLog));
        when(userRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(userModel));
        when(bookRepository.findByIdAndSoftDeleteIsFalse(id)).thenReturn(Optional.ofNullable(book));
        bookService.bookSaleLog(id, userId);
        //the
        assert userModel != null;
        verify(userRepository,times(1)).save(userModel);
        verify(bookRepository,times(1)).findByIdAndSoftDeleteIsFalse(id);
        assert bookPurchaseLog != null;
        verify(bookPurchaseLogRepository,times(1)).save(bookPurchaseLog);
    }



}