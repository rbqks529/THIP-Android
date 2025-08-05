package com.texthip.thip.data.model.service

import com.texthip.thip.data.model.base.BaseResponse
import com.texthip.thip.data.model.book.response.BookListResponse
import com.texthip.thip.data.model.book.response.BookSearchData
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {

    @GET("books")
    suspend fun getBooks(
        @Query("type") type: String  // "saved" 또는 "joining"
    ): BaseResponse<BookListResponse>

    @GET("books")
    suspend fun searchBooks(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1
    ): BaseResponse<BookSearchData>
}