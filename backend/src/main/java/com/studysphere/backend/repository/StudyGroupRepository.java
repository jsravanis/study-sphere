package com.studysphere.backend.repository;

import com.studysphere.backend.model.StudyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudyGroupRepository extends JpaRepository<StudyGroup, Long> {
    Optional<StudyGroup> findById(Long id);

    @Query("SELECT sg FROM StudyGroup sg WHERE sg.isPrivate = false " +
            "AND sg.currentUserCount < sg.maxLimit " +
            "AND NOT EXISTS (SELECT ur FROM UserRole ur WHERE ur.studyGroup = sg AND ur.user.id = :userId)")
    List<StudyGroup> findPublicGroupsNotJoined(@Param("userId") Long userId);


}
