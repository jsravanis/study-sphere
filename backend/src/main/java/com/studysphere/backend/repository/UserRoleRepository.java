package com.studysphere.backend.repository;

import com.studysphere.backend.model.StudyGroup;
import com.studysphere.backend.model.User;
import com.studysphere.backend.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    List<UserRole> findByStudyGroupId(Long studyGroupId);
}
