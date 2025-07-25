package xyz.arinmandri.playground.service.board;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepo extends JpaRepository<Post, Long>
{}
