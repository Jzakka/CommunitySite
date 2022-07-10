package CommunitySIte.demo.web.controller.page;

import lombok.Getter;

@Getter
public class Criteria {

    private int page = 1;
    private int perPageNum = 10;

    //쿼리에서 시작 페이지 행 넘버
    public int pageStart() {
        return (page - 1) * perPageNum;
    }

    public void setPage(int page) {
        this.page = page<=0?1:page;
    }

    public void setPerPageNum(int pageCount) {
        this.perPageNum = pageCount!=perPageNum?perPageNum:pageCount;
    }
}
