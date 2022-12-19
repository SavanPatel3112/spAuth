package com.example.authmoduls.auth.service.bookService;

import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.auth.model.Book;
import com.example.authmoduls.auth.model.UserModel;
import com.example.authmoduls.auth.repository.bookRepository.BookPurchaseLogRepository;
import com.example.authmoduls.auth.repository.bookRepository.BookRepository;
import com.example.authmoduls.auth.repository.userRepository.UserRepository;
import com.example.authmoduls.auth.service.userService.UserService;
import com.example.authmoduls.common.constant.MessageConstant;
import com.example.authmoduls.common.decorator.FilterSortRequest;
import com.example.authmoduls.common.decorator.NullAwareBeanUtilsBean;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.exception.InvaildRequestException;
import com.example.authmoduls.common.exception.NotFoundException;
import com.example.authmoduls.common.model.AdminConfiguration;
import com.example.authmoduls.common.model.EmailModel;
import com.example.authmoduls.common.utils.ExcelUtil;
import com.example.authmoduls.common.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final NullAwareBeanUtilsBean nullAwareBeanUtilsBean;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final BookPurchaseLogRepository bookPurchaseLogRepository;
    private final UserRepository userRepository;
    private final AdminConfiguration adminConfiguration;
    private final Utils utils;
    public BookServiceImpl(BookRepository bookRepository, NullAwareBeanUtilsBean nullAwareBeanUtilsBean, ModelMapper modelMapper, UserService userService, BookPurchaseLogRepository bookPurchaseLogRepository, UserRepository userRepository, AdminConfiguration adminConfiguration, Utils utils) {
        this.bookRepository = bookRepository;
        this.nullAwareBeanUtilsBean = nullAwareBeanUtilsBean;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.bookPurchaseLogRepository = bookPurchaseLogRepository;
        this.userRepository = userRepository;
        this.adminConfiguration = adminConfiguration;
        this.utils = utils;
    }

    private Book getBook(String id) {
        return bookRepository.findByIdAndSoftDeleteIsFalse(id).orElseThrow(() -> new NotFoundException(MessageConstant.BOOK_ID_NOT_FOUND));
    }

    @Override
    public BookResponse addOrUpdateBook(BookAddRequest bookAddRequest, String id, Role role) throws InvocationTargetException, IllegalAccessException {
        if (id != null) {
            Book book = getBook(id);
            nullAwareBeanUtilsBean.copyProperties(book, bookAddRequest);
            bookRepository.save(book);
            return modelMapper.map(book, BookResponse.class);
        } else {
            if (role == null)//check user plan(role)
                throw new InvaildRequestException(MessageConstant.ROLE_NOT_FOUND);
        }
        Book book = new Book();
        book.setRole(role);
        book.setDate(new Date());
        nullAwareBeanUtilsBean.copyProperties(book, bookAddRequest);
        bookRepository.save(book);
        return modelMapper.map(book, BookResponse.class);
    }


    @Override
    public void bookPurchaseLog(String id, String userId) {
        Book book = getBook(id);
        UserModel userModel = userService.getUserModel(userId);
        if (userModel.getBalance() < book.getPrice()) {
            throw new NotFoundException(MessageConstant.NOT_VALID_BALANCE);
        }
        BookPurchaseLog bookPurchaseLog = new BookPurchaseLog();
        double amount = userModel.getBalance() - book.getPrice();
        //object bookPurchaseLog
        //ste -- bookName, bookId,getPrice,userId,setDate
        //create bookPurchaseLogRepository
        //save bookPurchaseLog
        userModel.setBalance(amount);
        userRepository.save(userModel);
        bookPurchaseLog.setBookId(id);
        bookPurchaseLog.setUserId(userId);
        bookPurchaseLog.setDiscount(book.getDiscount());
        bookPurchaseLog.setBookName(book.getBookName());
        bookPurchaseLog.setRefundDiscount(book.getRefundDiscount());
        bookPurchaseLog.setPrice(book.getPrice());
        bookPurchaseLog.setDate(new Date());
        bookPurchaseLog.setSoftDelete(false);
        bookPurchaseLogRepository.save(bookPurchaseLog);
    }

    @Override
    public BookPurchaseDetail bookChartApi(int year) throws JSONException {
        BookPurchaseDetail bookPurchaseDetail = new BookPurchaseDetail();
        List<BookData> bookDataList;
        List<BookPurchase> bookPurchases = new ArrayList<>(bookRepository.bookChartApi(year));
        log.info("bookPurchase:{}", bookPurchases);
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
        int totalCount = 0;
        HashMap<String, String> bookNameHashMap = new LinkedHashMap<>();
        bookNameHashMap.put("1", "SpringBoot");
        bookNameHashMap.put("2", "Java");
        bookNameHashMap.put("3", "The Exiled");
        bookNameHashMap.put("4", "The Complete Harry Potter");
        bookNameHashMap.put("5", "Antonina Or The Fall Of Rome");
        if (!CollectionUtils.isEmpty(bookPurchases)) {
            log.info("bookPurchase: {}", bookPurchases.size());
            for (BookPurchase bookPurchase : bookPurchases) {
                bookDataList = bookPurchase.getBookData();
                List<String> bookName = bookDataList.stream().map(BookData::getBookName).collect(Collectors.toList());
                log.info("Book Details:{}", bookName);
                checkBookNameExist(bookName, bookNameHashMap, bookDataList, bookPurchase);
            }
        }
        for (Map.Entry<String, Integer> entry : month.entrySet()) {
            String titleName = entry.getKey() + "-" + year;
            title.add(titleName);
            bookPurchaseDetail.setTitle(title);
            boolean exist = bookPurchases.stream().anyMatch(e -> e.get_id() == entry.getValue());
            if (!exist) {
                List<String> name = new ArrayList<>();
                List<BookData> bookDetails = new ArrayList<>();
                BookPurchase bookPurchase1 = new BookPurchase();
                bookPurchase1.set_id(entry.getValue());
                bookPurchase1.setTotalCount(0);
                checkBookNameExist(name, bookNameHashMap, bookDetails, bookPurchase1);
                bookPurchase1.setBookData(bookDetails);
                bookPurchases.add(bookPurchase1);
            }
        }
        bookPurchases.sort(Comparator.comparing(BookPurchase::get_id));
        bookPurchaseDetail.setBookDataResponse(bookPurchases);
        bookPurchaseDetail.setTitle(title);
        for (BookPurchase bookPurchase : bookPurchases) {
            totalCount = totalCount + bookPurchase.getTotalCount();
            bookPurchaseDetail.setTotalCount(totalCount);
        }
        log.info("totalCount:{}", totalCount);
        bookPurchaseDetail.setTotalCount(totalCount);
        return bookPurchaseDetail;
    }

    @Override
    public void bookSaleLog(String bookId, String userId) {
        Optional<BookPurchaseLog> bookPurchaseLog = bookPurchaseLogRepository.findFirstByBookIdAndUserIdAndSoftDeleteIsFalse(bookId, userId);
        log.info("book" + bookPurchaseLog);
        if (bookPurchaseLog.isPresent()) {
            BookPurchaseLog bookPurchaseLog1 = bookPurchaseLog.get();
            UserModel userModel = userService.getUserModel(userId);
            double total = bookPurchaseLog1.getPrice() * bookPurchaseLog1.getRefundDiscount() / 100;
            Book book = getBook(bookId);
            total = book.getPrice() - total;
            double amount = userModel.getBalance() + total;
            userModel.setBalance(amount);
            userRepository.save(userModel);
            bookPurchaseLog1.setResale(true);
            bookPurchaseLogRepository.save(bookPurchaseLog1);
        }
    }

    @Override
    public Workbook getBookData() throws JSONException, InvocationTargetException, IllegalAccessException {
        HashMap<String, List<BookResponseExcel>> hashMap = new LinkedHashMap<>();
        List<BookDetailsData> bookDetailsDataList = bookRepository.bookData();
        log.info("bookDetailDataList:{}", bookDetailsDataList);
        if (CollectionUtils.isNotEmpty(bookDetailsDataList)) {
            for (BookDetailsData bookDetailsData : bookDetailsDataList) {
                List<BookResponseExcel> bookResponseExcels = new ArrayList<>();
                for (BookDetails bookDatum : bookDetailsData.getBookData()) {
                    BookResponseExcel bookResponseExcel = new BookResponseExcel();
                    nullAwareBeanUtilsBean.copyProperties(bookResponseExcel, bookDatum);
                    bookResponseExcels.add(bookResponseExcel);
                }
                hashMap.put(bookDetailsData.getBookName(), bookResponseExcels);
            }
        }
        log.info("hashMap:{}", hashMap);
        return ExcelUtil.createWorkbookFromBookDetailsData(hashMap);
    }

    @Override
    public Workbook getBookDataWithMonthAndYear(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws InvocationTargetException, IllegalAccessException, JSONException {
        Page<BooksList> booksLists = bookRepository.bookDetailsWithMonthAndYear(bookFilter, sort, pagination);
        List<BooksList> booksLists1 = new ArrayList<>(booksLists.getContent());
        List<BookDataResponseExcel> bookDataResponseExcels = new ArrayList<>();
        String title = "Exported date" + bookFilter.getMonth();
        for (BooksList booksList : booksLists1) {
            BookDataResponseExcel bookDataResponseExcel = new BookDataResponseExcel();
            nullAwareBeanUtilsBean.copyProperties(bookDataResponseExcel, booksList);
            bookDataResponseExcels.add(bookDataResponseExcel);
        }
        return ExcelUtil.createWorkbookFromData(bookDataResponseExcels, title);
    }

    @Override
    public Workbook getUserBookData() throws JSONException, InvocationTargetException, IllegalAccessException, IOException {
        HashMap<String, List<UserBookResponseExcel>> hashMap = new LinkedHashMap<>();
        List<UserBookDetailsData> userBookDetailsData = bookRepository.userBookData();
        if (CollectionUtils.isNotEmpty(userBookDetailsData)) {
            for (UserBookDetailsData userBookDetailsData1 : userBookDetailsData) {
                List<UserBookResponseExcel> userBookResponseExcels = new ArrayList<>();
                for (UserBooksDetails userBooksDetails : userBookDetailsData1.getUserBookData()) {
                    UserBookResponseExcel userBookResponseExcel = new UserBookResponseExcel();
                    nullAwareBeanUtilsBean.copyProperties(userBookResponseExcel, userBooksDetails);
                    userBookResponseExcels.add(userBookResponseExcel);
                }
                hashMap.put(userBookDetailsData1.getFullName(), userBookResponseExcels);
            }
        }
        Workbook workbook = ExcelUtil.createWorkbookFromUserBookDetailsData(hashMap);
        ByteArrayResource byteArrayResource = ExcelUtil.getBiteResourceFromWorkbook(workbook);
        File file = new File(" C:\\excelFiles\\demo.xlsx ");
        FileUtils.writeByteArrayToFile(file, byteArrayResource.getByteArray());
        file.createNewFile();
        EmailModel emailModel = new EmailModel();
        emailModel.setTo(adminConfiguration.getTechAdmins().iterator().next());
        emailModel.setCc(adminConfiguration.getTechAdmins());
        emailModel.setFile(file);
        emailModel.setSubject("USer Detail");
        utils.sendEmailNow(emailModel);
        file.deleteOnExit();
        return workbook;
    }

    @Override
    public Page<BooksList> getBookListByPagination(BookFilter bookFilter, FilterSortRequest.SortRequest<BookSortBy> sort, PageRequest pagination) throws JSONException {
        return bookRepository.bookDetailsWithMonthAndYear(bookFilter, sort, pagination);
    }

    @Override
    public List<BookTotalCountWithMonth> bookDataWithMonthAndYearAndTotalPrice(int month, int year) throws JSONException {
        return bookRepository.bookDataWithMonthAndYearAndTotalPrice(month, year);
    }

    void checkBookNameExist(List<String> bookDetail, HashMap<String, String> bookName, List<BookData> bookDataLists, BookPurchase bookPurchase) {
        for (Map.Entry<String, String> entry : bookName.entrySet()) {
            if (!bookDetail.contains(entry.getValue())) {
                BookData bookData = new BookData();
                bookData.setBookName(entry.getValue());
                bookData.setCount(0);
                bookDataLists.add(bookData);
            }
        }
        bookDataLists.sort(Comparator.comparing(BookData::getBookName));
        bookPurchase.setBookData(bookDataLists);
    }
}
