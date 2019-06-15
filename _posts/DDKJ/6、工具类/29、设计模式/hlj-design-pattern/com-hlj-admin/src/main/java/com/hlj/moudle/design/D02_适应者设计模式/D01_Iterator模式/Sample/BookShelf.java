package com.hlj.moudle.design.D02_适应者设计模式.D01_Iterator模式.Sample;


import java.util.ArrayList;
import java.util.List;

/**
 * 表示书架的类
 */
public class BookShelf implements Aggregate {

    private List<Book> books;
    private int last = 0; //书架的长度，从开始

    public BookShelf(){
        books = new ArrayList<>() ;
    }

    /**
     * 获取某一个位置index 的书
     * @param index
     * @return
     */
    public Book getBookAt(int index) {
        return books.get(index);
    }

    /**
     * 书架上放书
     * @param book
     */
    public void appendBook(Book book) {
        this.books.add(book);
        last++;
    }

    /**
     * 获取目前书架长度
     * @return
     */
    public int getLength() {
        return last;
    }

    /**
     * 获取遍历该书架的迭代器
     * @return
     */
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }
}
