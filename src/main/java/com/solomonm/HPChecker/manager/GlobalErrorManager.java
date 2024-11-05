package com.solomonm.HPChecker.manager;

import com.solomonm.HPChecker.config.ErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 에러코드를 전역관리하는 클래스로 에러 추가 조회 가능
 * 사용 X
 *
 * @author 김상준
 * @version 1.0
 */
public class GlobalErrorManager {
    // 싱글톤 패턴을 위한 인스턴스 생성
    private static final GlobalErrorManager instance = new GlobalErrorManager();
    private final List<ErrorCode> errorCodeList = new ArrayList<>();

    private GlobalErrorManager() {}

    /**
     * 싱글톤 인스턴스를 반환하는 메서드
     *
     * @return GlobalErrorManager의 인스턴스
     */
    public static GlobalErrorManager getInstance() {
        return instance;
    }

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
}
