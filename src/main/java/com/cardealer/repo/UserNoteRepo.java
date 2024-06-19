package com.cardealer.repo;

import com.cardealer.model.UserNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNoteRepo extends JpaRepository<UserNote, Long> {
}