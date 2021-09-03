package com.oneinch.config

interface IResources<T> {
    fun load(): T
}