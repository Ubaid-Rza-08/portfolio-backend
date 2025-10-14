package com.example.backend.repository;

import com.example.backend.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, UUID> {
    List<TeamMember> findByUserIdAndIsActiveTrueOrderBySortOrder(UUID userId);
    List<TeamMember> findByDepartment(String department);
    List<TeamMember> findByLocation(String location);
}
