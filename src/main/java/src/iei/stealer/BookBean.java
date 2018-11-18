/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src.iei.stealer;

public class BookBean {

    private String page;
    private String title;
    private String author;
    private String price;

    public BookBean(String page) {
        this.page = page;
        this.title = "";
        this.author = "";
        this.price = "";
    }

    public void setUnknown() {
        this.title = "X";
        this.author = "X";
        this.price = "X";
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

}
