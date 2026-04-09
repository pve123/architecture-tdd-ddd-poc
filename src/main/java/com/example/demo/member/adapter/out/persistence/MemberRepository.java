package com.example.demo.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<MemberJpaEntity, String> {

    Optional<MemberJpaEntity> findByIdAndIsDeletedFalse(String id);

}
