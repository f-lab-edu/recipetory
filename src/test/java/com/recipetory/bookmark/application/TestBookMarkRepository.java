package com.recipetory.bookmark.application;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class TestBookMarkRepository implements BookMarkRepository {
    private final List<BookMark> bookMarks = new ArrayList<>();
    private final AtomicLong atomicLong = new AtomicLong(1L);

    @Override
    public BookMark save(BookMark bookMark) {
        BookMark saved = BookMark.builder().id(atomicLong.getAndIncrement())
                        .bookMarker(bookMark.getBookMarker()).recipe(bookMark.getRecipe())
                        .build();
        bookMarks.add(saved);
        return saved;
    }

    @Override
    public List<BookMark> findByBookMarker(User bookMarker) {
        return bookMarks.stream().filter(bookMark ->
                bookMark.getBookMarker().equals(bookMarker))
                .toList();
    }

    @Override
    public List<BookMark> findByRecipe(Recipe recipe) {
        return bookMarks.stream().filter(bookMark ->
                        bookMark.getRecipe().equals(recipe))
                .toList();
    }

    @Override
    public Optional<BookMark> findByBookMarkerAndRecipe(User bookMarker, Recipe recipe) {
        return bookMarks.stream().filter(bookMark ->
                        bookMark.getBookMarker().equals(bookMarker)
                                && bookMark.getRecipe().equals(recipe))
                .findAny();
    }

    @Override
    public Optional<BookMark> findById(Long id) {
        return bookMarks.stream().filter(bookMark ->
                        bookMark.getId().equals(id))
                .findAny();
    }

    @Override
    public void deleteBookMark(BookMark bookMark) {
        bookMarks.remove(bookMark);
    }
}
