package io.github.pve123.hexagonal.board.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<BoardJpaEntity, String> {

    Optional<BoardJpaEntity> findByIdAndIsDeletedFalse(String id);
}
