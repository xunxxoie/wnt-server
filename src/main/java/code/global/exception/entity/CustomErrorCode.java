package code.global.exception.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CustomErrorCode implements ErrorCode{

    // Global
    DATA_VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "GLOBAL400_1", "요청 데이터 유효성 검사에 실패하였습니다."),
    SQL_OPERATION_FAILED(HttpStatus.BAD_REQUEST, "GLOBAL400_2", "요청 데이터 데이터베이스 작업에 실패하였습니다."),
    NOT_VALID_API_ENDPOINT(HttpStatus.NOT_FOUND, "GLOBAL404_1", "유효하지 않은 API 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL500_1", "서버 내부에서 처리할 수 없는 오류가 발생하였습니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER404_1", "유저 조회에 실패하였습니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "USER400_1", "중복되는 이메일입니다."),
    PASSWORD_NOT_MATCHED(HttpStatus.BAD_REQUEST, "USER400_2", "일치하지 않는 비밀번호입니다."),
    PROVIDER_ID_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "USER400_3", "유효하지 않은 Provider Id 입니다."),

    // Mail
    SEND_MAIL_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "MAIL503_1", "메일 전송에 실패하였습니다."),
    NO_AUTH_CODE_EMAIL(HttpStatus.BAD_REQUEST, "MAIL400_1", "메일 인증을 시도하지 않은 이메일입니다."),
    NOT_VERIFIED_EMAIL(HttpStatus.UNAUTHORIZED, "MAIN401_1", "메일 인증에 실패한 이메일입니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
