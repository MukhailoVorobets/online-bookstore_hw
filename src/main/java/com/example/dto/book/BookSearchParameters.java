package com.example.dto.book;

public record BookSearchParameters(String[] title, String[] author, String[] isbn) {
}
