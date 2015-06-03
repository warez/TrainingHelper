package it.traininghelper.valueobject;

import java.util.List;

/**
 * Created by warez on 29/05/15.
 */
public class PageableResult<E> {

    private int page;
    private int count;
    private List<E> result;

    public PageableResult(int page, int count, List<E> result) {
        this.page = page;
        this.count = count;
        this.result = result;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<E> getResult() {
        return result;
    }

    public void setResult(List<E> result) {
        this.result = result;
    }
}
