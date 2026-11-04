package io.github.pve123.hexagonal.member.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface MemberJpaRepository extends JpaRepository<MemberJpaEntity, String> {

    Optional<MemberJpaEntity> findByIdAndIsDeletedFalse(String id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, String id);
}
