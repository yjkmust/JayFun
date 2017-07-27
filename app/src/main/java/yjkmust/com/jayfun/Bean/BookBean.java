package yjkmust.com.jayfun.Bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableInt;

import com.android.databinding.library.baseAdapters.BR;

import java.io.Serializable;
import java.util.List;

/**
 * Created by GEOFLY on 2017/7/6.
 */

public class BookBean extends BaseObservable implements Serializable{
//    private ObservableInt  count = new ObservableInt();
    @Bindable
 private int count;
    @Bindable
 private int start;
    @Bindable
 private int total;
    @Bindable
 private List<BooksBean> books;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
        notifyPropertyChanged(BR.count);
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
        notifyPropertyChanged(BR.start);
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
        notifyPropertyChanged(BR.total);
    }

    public List<BooksBean> getBooks() {
        return books;
    }

    public void setBooks(List<BooksBean> books) {
        this.books = books;
        notifyPropertyChanged(BR.books);
    }
}
