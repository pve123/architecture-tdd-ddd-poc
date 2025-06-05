package com.example.demo.board.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardJpaEntity, String> {
}
