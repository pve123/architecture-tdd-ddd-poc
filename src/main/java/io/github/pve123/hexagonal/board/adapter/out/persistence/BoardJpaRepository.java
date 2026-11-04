package io.github.pve123.hexagonal.board.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface BoardJpaRepository extends JpaRepository<BoardJpaEntity, String> {

    Optional<BoardJpaEntity> findByIdAndIsDeletedFalse(String id);
}
