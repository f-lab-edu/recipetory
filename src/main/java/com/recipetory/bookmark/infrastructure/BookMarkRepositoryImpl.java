package com.recipetory.bookmark.infrastructure;

import com.recipetory.bookmark.domain.BookMark;
import com.recipetory.bookmark.domain.BookMarkRepository;
import com.recipetory.recipe.domain.Recipe;
import com.recipetory.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookMarkRepositoryImpl implements BookMarkRepository {
    private final BookMarkJpaRepository bookMarkJpaRepository;

    @Override
    public BookMark save(BookMark bookMark) {
        return bookMarkJpaRepository.save(bookMark);
    }

    @Override
    public List<BookMark> findByBookMarker(User bookMarker) {
        return bookMarkJpaRepository.findByBookMarker(bookMarker);
    }

    @Override
    public List<BookMark> findByRecipe(Recipe recipe) {
        return bookMarkJpaRepository.findByRecipe(recipe);
    }

    @Override
    public Optional<BookMark> findByBookMarkerAndRecipe(User bookMarker, Recipe recipe) {
        return bookMarkJpaRepository.findByBookMarkerAndRecipe(bookMarker,recipe);
    }

    @Override
    public Optional<BookMark> findById(Long id) {
        return bookMarkJpaRepository.findById(id);
    }

    @Override
    public void deleteBookMark(BookMark bookMark) {
        bookMarkJpaRepository.delete(bookMark);
    }
}
