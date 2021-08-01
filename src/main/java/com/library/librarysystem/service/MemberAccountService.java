package com.library.librarysystem.service;

import com.library.librarysystem.model.Address;
import com.library.librarysystem.model.MemberAccount;
import com.library.librarysystem.model.Person;

import java.util.List;

public interface MemberAccountService {

    MemberAccount createMemberAccount(Person person);

    MemberAccount addMemberToBlacklist(Long memberId);

    MemberAccount blockMemberAccount(Long memberId);

    MemberAccount changeAddress(Long memberId, Address address);

    MemberAccount changeEmail(Long memberId, String email);

    List<MemberAccount> getAllMembers();

    void deleteMemberAccount(Long memberId);

}
