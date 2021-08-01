package com.library.librarysystem.controller;

import com.library.librarysystem.model.Address;
import com.library.librarysystem.model.MemberAccount;
import com.library.librarysystem.model.Person;
import com.library.librarysystem.service.MemberAccountService;
import com.library.librarysystem.service.impl.MemberAccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
public class MemberAccountController {

    MemberAccountService memberAccountService;

    @Autowired
    public MemberAccountController(MemberAccountServiceImpl memberAccountService) {
        this.memberAccountService = memberAccountService;
    }

    @PostMapping("/create")
    public ResponseEntity<MemberAccount> createMemberAccount(@RequestBody Person person) {
        return new ResponseEntity<>(memberAccountService.createMemberAccount(person), HttpStatus.CREATED);
    }

    @PostMapping("/blacklist/{memberId}")
    public ResponseEntity<MemberAccount> addMemberToBlacklist(@PathVariable Long memberId){
        return new ResponseEntity<>(memberAccountService.addMemberToBlacklist(memberId), HttpStatus.OK);
    }

    @PostMapping("/block/{memberId}")
    public ResponseEntity<MemberAccount> blockMemberAccount(@PathVariable Long memberId){
        return new ResponseEntity<>(memberAccountService.blockMemberAccount(memberId), HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<MemberAccount>> getAllMembers(){
        return new ResponseEntity<>(memberAccountService.getAllMembers(), HttpStatus.OK);
    }

    @PutMapping("/{memberId}/change/address")
    public ResponseEntity<MemberAccount> changeMemberAddress(@PathVariable Long memberId, @RequestBody Address address){
        return new ResponseEntity<>(memberAccountService.changeAddress(memberId, address), HttpStatus.OK);
    }

    @PutMapping("/{memberId}/change")
    public ResponseEntity<MemberAccount> changeMemberAddress(@PathVariable Long memberId, @RequestParam String email){
        return new ResponseEntity<>(memberAccountService.changeEmail(memberId, email), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{memberId}")
    public ResponseEntity<String> deleteMemberAccount(@PathVariable Long memberId){
        memberAccountService.deleteMemberAccount(memberId);
        return new ResponseEntity<>("Member account has been deleted.", HttpStatus.OK);
    }
}
