package org.mai.dep810.rest_api_io_lesson;

public enum HttpStatus {
    NOT_FOUND(404),
    REDIRECT(302),
    SERVER_ERROR(500),
    OK(200)
    ;

    private int code;

    public int getCode() {
        return code;
    }

    HttpStatus(int code) {
        this.code = code;
    }

}
