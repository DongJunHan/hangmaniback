package com.project.hangmani.exception;

public class NotFoundAttachment extends NotFoundException{
    public NotFoundAttachment() {
        super("첨부파일 찾을 수 없습니다");
    }

}
