package CommunitySIte.demo.web.controller.access;

import CommunitySIte.demo.domain.Accessible;
import CommunitySIte.demo.domain.PostType;
import CommunitySIte.demo.domain.Users;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AccessibilityChecker {
    public static boolean accessible(Users user, Accessible object) {
        //로그인한 사용자가 유동글 지우려려하면 리다이렉트->일반 사용자도 유동글 삭제 관여 가능
        /*if(object.getPostType()==PostType.ANONYMOUS && user !=null){
            log.info("accessible : 로그인 사용자가 글 수정접근");
            return false;
        }*/
        //유동이 일반 글 지우려하면 리다이렉트
        if(object.getPostType()== PostType.NORMAL && user ==null ){
            log.info("accessible : 비로그인 사용자가 글 수정접근");
            return false;
        }
        //타임리프에서 이미 검증하지만 서버쪽에서도 이중 검증
        //일반 사용자가 자기글 아닌거 지우려 하면 리다이렉트
        else{
            if(!(object.getUser()==null || object.getUser().equals(user))){
                log.info("accessible : 작성자 불일치");
                //리다이렉트 말고 오류메시지나 오류창으로 넘어가게 만들기
                return false;
            }
        }
        return true;
    }
}
