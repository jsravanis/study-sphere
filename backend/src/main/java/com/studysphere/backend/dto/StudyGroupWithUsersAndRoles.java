package com.studysphere.backend.dto;

import com.studysphere.backend.model.StudyGroup;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class StudyGroupWithUsersAndRoles {
    private StudyGroup studyGroup;
    private List<UserWithRole> usersWithRoles;
}