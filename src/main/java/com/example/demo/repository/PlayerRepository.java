package com.example.demo.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}