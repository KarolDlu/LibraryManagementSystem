package com.library.librarysystem.repository;

import com.library.librarysystem.model.MemberAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberAccountRepo extends JpaRepository<MemberAccount, Long> {
}
