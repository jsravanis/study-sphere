package com.studysphere.backend.repository;

import com.studysphere.backend.model.InvitationStatus;
import com.studysphere.backend.model.StudyGroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyGroupInvitationRepository extends JpaRepository<StudyGroupInvitation, Long> {

}
