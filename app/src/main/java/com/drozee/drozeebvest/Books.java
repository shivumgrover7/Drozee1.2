package com.drozee.drozeebvest;

public class Books {
    String bookname, author;

    public Books(String bookname, String author) {
        this.bookname = bookname;
        this.author = author;
    }
    public Books(){}

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
