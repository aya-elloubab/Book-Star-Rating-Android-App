package com.example.starratingsapp.services;

import com.example.starratingsapp.beans.BookStar;
import com.example.starratingsapp.dao.IDAO;

import java.util.ArrayList;
import java.util.List;

public class BookStarService implements IDAO<BookStar> {
    private List<BookStar> bookStars;
    private static BookStarService instance;
    private BookStarService() {
        this.bookStars = new ArrayList<>();
    }
    public static BookStarService getInstance() {
        if(instance == null)
            instance = new BookStarService();
        return instance;
    }

    @Override
    public boolean create(BookStar o){return bookStars.add(o);}
    @Override
    public boolean update(BookStar o) {
        for(BookStar s : bookStars){
            if(s.getId() == o.getId()){
                s.setImg(o.getImg());
                s.setName(o.getName());
                s.setStar(o.getStar());
            }
        }
        return true;
    }
    @Override
    public boolean delete(BookStar o) {
        return bookStars.remove(o);
    }
    @Override
    public BookStar findById(int id) {
        for(BookStar s : bookStars){
            if(s.getId() == id)
                return s;
        }
        return null;
    }
    @Override
    public List<BookStar> findAll() {
        return bookStars;
    }


}
