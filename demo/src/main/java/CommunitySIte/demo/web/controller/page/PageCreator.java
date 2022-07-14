package CommunitySIte.demo.web.controller.page;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageCreator {

    private Criteria criteria;
    private int totalCount;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private final int displayPageNum = 10;

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (this.totalCount == 0) {
            start = 1;
            end = 1;
            prev = false;
            next = false;
            return;
        }
        calcData();
    }

    private void calcData() {
        end = (int) (Math.ceil(criteria.getPage() / (double) displayPageNum) * displayPageNum);

        start = (end - displayPageNum) + 1;
        start = start <= 0 ? 1 : start;

        int tempEnd = (int) (Math.ceil(totalCount / (double) criteria.getPerPageNum()));
        end = end > tempEnd ? tempEnd : end;

        prev = start == 1 ? false : true;
        next = end * criteria.getPerPageNum() < totalCount ? true : false;
    }

    public static PageCreator newPageCreator(Integer page, Criteria criteria, Integer postsCount) {
        criteria.setPage(page == null ? 1 : page);
        log.info("criteria.page={}", criteria.getPage());

        PageCreator pageCreator = new PageCreator();
        pageCreator.setCriteria(criteria);
        pageCreator.setTotalCount(postsCount);
        return pageCreator;
    }
}
