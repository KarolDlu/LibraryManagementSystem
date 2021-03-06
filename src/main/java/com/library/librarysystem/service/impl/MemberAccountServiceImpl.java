package com.library.librarysystem.service.impl;

import com.library.librarysystem.exceptions.ObjectNotFoundException;
import com.library.librarysystem.exceptions.PersonAlreadyExistsException;
import com.library.librarysystem.model.AccountStatus;
import com.library.librarysystem.model.Address;
import com.library.librarysystem.model.MemberAccount;
import com.library.librarysystem.model.Person;
import com.library.librarysystem.repository.MemberAccountRepo;
import com.library.librarysystem.repository.PersonRepo;
import com.library.librarysystem.service.MemberAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MemberAccountServiceImpl implements MemberAccountService {

    PersonRepo personRepo;
    MemberAccountRepo memberAccountRepo;

    @Autowired
    public MemberAccountServiceImpl(PersonRepo personRepo, MemberAccountRepo memberAccountRepo) {
        this.personRepo = personRepo;
        this.memberAccountRepo = memberAccountRepo;
    }

    @Override
    public MemberAccount createMemberAccount(Person person) {
        Optional<Person> optPerson = personRepo.findPersonByEmail(person.getEmail());
        return (MemberAccount) optPerson.map(personFromDB -> {
            throw new PersonAlreadyExistsException(person.getEmail());
        }).orElseGet(() -> {
            Person newPerson = personRepo.save(person);
            MemberAccount memberAccount = new MemberAccount(AccountStatus.ACTIVE, newPerson, Date.valueOf(LocalDateTime.now().toLocalDate()));
            return memberAccountRepo.save(memberAccount);
        });
    }

    @Override
    public MemberAccount addMemberToBlacklist(Long memberId) {
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        return optMember.map(memberAccount -> {
            MemberAccount member = optMember.get();
            member.addToBlacklist();
            return memberAccountRepo.save(member);
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("Member account", memberId);
        });
    }

    @Override
    public MemberAccount blockMemberAccount(Long memberId) {
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        return optMember.map(member -> {
            member.blockAccount();
            return memberAccountRepo.save(member);
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("Member account", memberId);
        });
    }

    @Override
    public List<MemberAccount> getAllMembers() {
        return memberAccountRepo.findAll();
    }

    @Override
    public MemberAccount changeAddress(Long memberId, Address address) {
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        return optMember.map(member -> {
            member.changeAddress(address);
            return memberAccountRepo.save(member);
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("Member account", memberId);
        });
    }

    @Override
    public MemberAccount changeEmail(Long memberId, String email) {
        Optional<MemberAccount> optMember = memberAccountRepo.findById(memberId);
        return optMember.map(member -> {
            member.changeEmail(email);
            return memberAccountRepo.save(member);
        }).orElseThrow(() -> {
            throw new ObjectNotFoundException("Member account", memberId);
        });
    }

    @Override
    public void deleteMemberAccount(Long memberId) {
        memberAccountRepo.deleteById(memberId);
    }
}
