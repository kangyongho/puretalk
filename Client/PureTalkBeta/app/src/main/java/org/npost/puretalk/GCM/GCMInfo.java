package org.npost.puretalk.GCM;

/**
 * GCM 관련 정보 변수/상수 선언
 */
public class GCMInfo {
    // GCM 서비스 이용시 Google Devoloper Consonle에 접속하여 프로젝트를 등록해야 합니다.
    // 자세한 사항은 https://developers.google.com/cloud-messaging 을 참고하세요.
    // gcm 서비스가 최근에 수정되었습니다. 설정파일을 손쉽게 json 파일로 다운받아 설정할 수 있습니다.
    // json 파일 위치는 왼쪽 탭에서 Project나 Packages로 바꾸면 src/google-services.json 파일로 저장되어있습니다.
    // json 파일이 없으면 Gradle 빌드시 Error 발생합니다.
    public static final String PROJECT_ID = "PROJECT_ID";
    public static final String GOOGLE_API_KEY = "GOOGLE_API_KEY";
}
