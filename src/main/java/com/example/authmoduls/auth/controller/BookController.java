package com.example.authmoduls.auth.controller;
import com.example.authmoduls.auth.decorator.bookDecorator.*;
import com.example.authmoduls.auth.service.bookService.BookService;
import com.example.authmoduls.common.constant.ResponseConstant;
import com.example.authmoduls.common.decorator.*;
import com.example.authmoduls.common.enums.Role;
import com.example.authmoduls.common.utils.Access;
import com.example.authmoduls.common.utils.ExcelUtil;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    RequestSession requestSession;
    @Autowired
    BookService bookService;
    @Autowired
    GeneralHelper generalHelper;
    @SneakyThrows
    @RequestMapping(name = "addOrUpdateBook", value = "/addOrUpdate", method = RequestMethod.POST)
    @Access(levels = {Role.ADMIN})
    public DataResponse<BookResponse> addOrUpdateBook(@RequestBody BookAddRequest bookAddRequest, @RequestParam(required = false) String id, @RequestParam(required = false) Role role) {
        DataResponse<BookResponse> dataResponse = new DataResponse<>();
        dataResponse.setData(bookService.addOrUpdateBook(bookAddRequest, id, role));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @SneakyThrows
    @Access(levels = {Role.STUDENT})
    @RequestMapping(name = "bookPurchaseLog ", value = "purchase/book", method = RequestMethod.POST)
    public DataResponse<Object> bookPurchaseLog(@RequestParam String id) {
        DataResponse<Object> bookResponse = new DataResponse<>();
        String userID = requestSession.getJwtUser().getId();
        bookService.bookPurchaseLog(id,userID);
        bookResponse.setStatus(Response.getOkResponse());
        return bookResponse;
    }
    @SneakyThrows
    @RequestMapping(name = "bookChartApi", value = "/chart", method = RequestMethod.GET)
    public DataResponse<BookPurchaseDetail> bookChartApi(@RequestParam int year) {
        DataResponse<BookPurchaseDetail> dataResponse = new DataResponse<>();
        dataResponse.setData(bookService.bookChartApi(year));
        dataResponse.setStatus(Response.getOkResponse());
        return dataResponse;
    }
    @SneakyThrows
    @Access(levels = {Role.STUDENT})
    @RequestMapping(name = "bookSaleLog ", value = "sale/book", method = RequestMethod.POST)
    public DataResponse<Object> bookSaleLog(@RequestParam String booKid) {
        DataResponse<Object> bookResponse = new DataResponse<>();
        String userId = requestSession.getJwtUser().getId();
        bookService.bookSaleLog(booKid, userId);
        bookResponse.setStatus(Response.getOkResponse());
        return bookResponse;
    }

    @SneakyThrows
    @RequestMapping(name = "getUserBookData", value = "/user/book/data", method = RequestMethod.GET)
    public ResponseEntity<Resource> getUserBookData() {
        Workbook workbook = bookService.getUserBookData();
        assert workbook != null;
        ByteArrayResource resource = ExcelUtil.getBiteResourceFromWorkbook(workbook);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "exported_user_book_data.xlsx" + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @SneakyThrows
    @RequestMapping(name = "getALlBookBYPagination" , value = "/getALlBook/filter",method = {RequestMethod.POST})
    @Access(levels = {Role.ADMIN,Role.DEPARTMENT})
    public PageResponse<BooksList> getBookListByPagination(@RequestBody FilterSortRequest<BookFilter, BookSortBy> filterSortRequest){
        PageResponse<BooksList> pageResponse = new PageResponse<>();
        BookFilter bookFilter = filterSortRequest.getFilter();
        Page<BooksList> bookList = bookService.getBookListByPagination(bookFilter,filterSortRequest.getSort(),
                generalHelper.getPagination(filterSortRequest.getPage().getPage(),filterSortRequest.getPage().getLimit()));
        pageResponse.setData(bookList);
        pageResponse.setStatus(Response.getSuccessResponse(ResponseConstant.SUCCESS));
        return pageResponse;
    }

    @SneakyThrows
    @RequestMapping(name = "getBookDataWithMonthAndYear", value = "/data/month", method = RequestMethod.POST)
    public ResponseEntity<Resource> getBookDataWithMonthAndYear(@RequestBody FilterSortRequest<BookFilter,BookSortBy> filterSortRequest) {
       /* filterSortRequest.getFilter();
        filterSortRequest.g();*/
        BookFilter bookFilter =  filterSortRequest.getFilter();
        FilterSortRequest.SortRequest<BookSortBy> sort= filterSortRequest.getSort();
        Pagination pagination=filterSortRequest.getPage();
        PageRequest pageRequest = PageRequest.of(pagination.getPage(),pagination.getLimit());
        Workbook workbook = bookService.getBookDataWithMonthAndYear(bookFilter,sort ,pageRequest);
        assert workbook != null;
        ByteArrayResource resource = ExcelUtil.getBiteResourceFromWorkbook(workbook);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "exported_book_data_month.xlsx" + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }



    @SneakyThrows
    @RequestMapping(name = "getBookData", value = "/data", method = RequestMethod.GET)
    public ResponseEntity<Resource> getBookData() {
        Workbook workbook = bookService.getBookData();
        assert workbook != null;
        ByteArrayResource resource = ExcelUtil.getBiteResourceFromWorkbook(workbook);
        return ResponseEntity.ok()
                .contentLength(resource.contentLength())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "exported_book_data.xlsx" + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(resource);
    }

    @SneakyThrows
    @RequestMapping(name = "bookDataWithMonthAndYearAndTotalPrice", value = "/totalPrice", method = RequestMethod.GET)
    public ListResponse<BookTotalCountWithMonth> bookDataWithMonthAndYearAndTotalPrice(@RequestParam int month ,int year) {
        ListResponse<BookTotalCountWithMonth> listResponse = new ListResponse<>();
        listResponse.setData(bookService.bookDataWithMonthAndYearAndTotalPrice(month,year));
        listResponse.setStatus(Response.getOkResponse());
        return listResponse;
    }


}
