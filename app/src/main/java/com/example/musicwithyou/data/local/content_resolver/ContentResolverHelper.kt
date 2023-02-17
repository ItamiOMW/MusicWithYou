package com.example.musicwithyou.data.local.content_resolver

interface ContentResolverHelper<T> {

    suspend fun getData(): List<T>


}