package com.solomonm.HPChecker.manager;

import com.solomonm.HPChecker.config.ErrorCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 에러코드를 전역관리하는 클래스로 에러 추가 조회 가능
 * [변경] 자바 싱글톤 패턴 -> 스프링 컨테이너
 *
 * @author 김상준
 * @version 1.0
 */
@Component
public class GlobalErrorManagerV2 {
    private final List<ErrorCode> errorCodeList = new ArrayList<>();

    /**
     * 에러 코드를 리스트에 추가하는 메서드
     *
     * @param errorCode 추가할 에러코드
     */
    public void addError(ErrorCode errorCode) {

        errorCodeList.add(errorCode);
    }

    /**
     * 저장된 모든 에러 코드를 반환하는 메서드
     *
     * @return 에러 코드 리스트
     */
    public List<ErrorCode> getErrors() {

        return new ArrayList<>(errorCodeList);
    }

    /**
     * 에러코드 리스트를 초기화 하는 메서드
     */
    public void clearErrors() {

        errorCodeList.clear();
    }

    /**
     * 에러의 유무를 확인하는 메서드
     *
     * @return boolean 에러의 유무
     */
    public boolean isEmpty() {

        return errorCodeList.isEmpty();
    }
}
