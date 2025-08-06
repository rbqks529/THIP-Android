package com.texthip.thip.data.model.repository

import com.texthip.thip.data.model.base.handleBaseResponse
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.book.response.BookDetail
import com.texthip.thip.data.model.book.response.BookSavedResponse
import com.texthip.thip.data.model.book.response.BookSearchData
import com.texthip.thip.data.model.book.response.MostSearchedBook
import com.texthip.thip.data.model.service.BookService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val bookService: BookService
) {

    // 저장된/모임 책 조회 API 연동
    suspend fun getBooks(type: String): Result<List<BookSavedResponse>> {
        return try {
            bookService.getBooks(type)
                .handleBaseResponse()
                .mapCatching { bookListResponse ->
                    bookListResponse?.bookList ?: emptyList()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 책 검색 API 연동
    suspend fun searchBooks(keyword: String, page: Int = 1): Result<BookSearchData> {
        return try {
            bookService.searchBooks(keyword, page)
                .handleBaseResponse()
                .mapCatching { bookSearchData ->
                    bookSearchData ?: throw Exception("책 검색 실패: 데이터가 없습니다")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 가장 많이 검색된 책 조회 API 연동
    suspend fun getMostSearchedBooks(): Result<List<MostSearchedBook>> {
        return try {
            bookService.getMostSearchedBooks()
                .handleBaseResponse()
                .mapCatching { mostSearchedBooks ->
                    mostSearchedBooks ?: emptyList()
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 책 상세 정보 조회 API 연동
    suspend fun getBookDetail(isbn: String): Result<BookDetail> {
        return try {
            bookService.getBookDetail(isbn)
                .handleBaseResponse()
                .mapCatching { bookDetail ->
                    bookDetail ?: throw Exception("책 정보를 찾을 수 없습니다")
                }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}