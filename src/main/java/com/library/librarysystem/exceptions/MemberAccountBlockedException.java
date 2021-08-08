package com.library.librarysystem.exceptions;

public class MemberAccountBlockedException extends RuntimeException {

    public MemberAccountBlockedException(Long memberId) {
        super("Member account with id: " + memberId + " is blocked.");
    }
}
