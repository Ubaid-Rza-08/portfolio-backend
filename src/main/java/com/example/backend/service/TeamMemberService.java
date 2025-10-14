package com.example.backend.service;

import com.example.backend.entity.TeamMember;
import com.example.backend.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TeamMemberService {

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    public TeamMember saveTeamMember(TeamMember teamMember) {
        return teamMemberRepository.save(teamMember);
    }

    public List<TeamMember> findActiveByUserId(UUID userId) {
        return teamMemberRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId);
    }

    public Optional<TeamMember> findById(UUID id) {
        return teamMemberRepository.findById(id);
    }

    public List<TeamMember> findByDepartment(String department) {
        return teamMemberRepository.findByDepartment(department);
    }

    public List<TeamMember> findByLocation(String location) {
        return teamMemberRepository.findByLocation(location);
    }

    public List<TeamMember> findAllByUserId(UUID userId) {
        return teamMemberRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId);
    }

    public TeamMember updateTeamMember(UUID id, TeamMember details) {
        return teamMemberRepository.findById(id)
                .map(member -> {
                    member.setName(details.getName());
                    member.setRole(details.getRole());
                    member.setBio(details.getBio());
                    member.setSkills(details.getSkills());
                    member.setExperienceYears(details.getExperienceYears());
                    member.setEmail(details.getEmail());
                    member.setLinkedinUrl(details.getLinkedinUrl());
                    member.setTwitterUrl(details.getTwitterUrl());
                    member.setPortfolioUrl(details.getPortfolioUrl());
                    member.setIsActive(details.getIsActive());
                    member.setSortOrder(details.getSortOrder());
                    member.setDepartment(details.getDepartment());
                    member.setLocation(details.getLocation());
                    member.setHourlyRate(details.getHourlyRate());
                    if (details.getImageUrl() != null && !details.getImageUrl().isEmpty()) {
                        member.setImageUrl(details.getImageUrl());
                    }
                    return teamMemberRepository.save(member);
                })
                .orElseThrow(() -> new RuntimeException("Team member not found with id: " + id));
    }

    public boolean deleteTeamMember(UUID id) {
        if (teamMemberRepository.existsById(id)) {
            teamMemberRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Missing methods that the controller needs
    public long getTeamMembersCount() {
        return teamMemberRepository.count();
    }

    public boolean existsById(UUID id) {
        return teamMemberRepository.existsById(id);
    }

    // Additional useful methods for team member management
    public List<TeamMember> findAll() {
        return teamMemberRepository.findAll();
    }

    public long getTeamMembersCountByUserId(UUID userId) {
        return teamMemberRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId).size();
    }

    public List<TeamMember> findByUserIdAndDepartment(UUID userId, String department) {
        List<TeamMember> allMembers = findActiveByUserId(userId);
        return allMembers.stream()
                .filter(member -> department.equals(member.getDepartment()))
                .toList();
    }

    public List<TeamMember> findByUserIdAndLocation(UUID userId, String location) {
        List<TeamMember> allMembers = findActiveByUserId(userId);
        return allMembers.stream()
                .filter(member -> location.equals(member.getLocation()))
                .toList();
    }

    public TeamMember toggleActiveStatus(UUID id) {
        return teamMemberRepository.findById(id)
                .map(member -> {
                    member.setIsActive(!member.getIsActive());
                    return teamMemberRepository.save(member);
                })
                .orElseThrow(() -> new RuntimeException("Team member not found with id: " + id));
    }

    public List<TeamMember> findBySkillsContaining(UUID userId, String skill) {
        List<TeamMember> allMembers = findActiveByUserId(userId);
        return allMembers.stream()
                .filter(member -> member.getSkills() != null &&
                        member.getSkills().toLowerCase().contains(skill.toLowerCase()))
                .toList();
    }

    public List<TeamMember> findByExperienceYearsGreaterThan(UUID userId, Integer years) {
        List<TeamMember> allMembers = findActiveByUserId(userId);
        return allMembers.stream()
                .filter(member -> member.getExperienceYears() != null &&
                        member.getExperienceYears() > years)
                .toList();
    }

    // Batch operations
    public List<TeamMember> saveAllTeamMembers(List<TeamMember> teamMembers) {
        return teamMemberRepository.saveAll(teamMembers);
    }

    public void deleteAllByUserId(UUID userId) {
        List<TeamMember> userTeamMembers = teamMemberRepository.findByUserIdAndIsActiveTrueOrderBySortOrder(userId);
        teamMemberRepository.deleteAll(userTeamMembers);
    }

    // Utility methods for sorting and ordering
    public void updateSortOrder(UUID id, Integer newSortOrder) {
        teamMemberRepository.findById(id)
                .ifPresent(member -> {
                    member.setSortOrder(newSortOrder);
                    teamMemberRepository.save(member);
                });
    }

//    public List<TeamMember> reorderTeamMembers(UUID userId, List<UUID> orderedIds) {
//        List<TeamMember> members = findActiveByUserId(userId);
//        for (int i = 0; i < orderedIds.size(); i++) {
//            UUID memberId = orderedIds.get(i);
//            members.stream()
//                    .filter(member -> member.getId().equals(memberId))
//                    .findFirst()
//                    .ifPresent(member -> {
//                        member.setSortOrder(i + 1);
//                        teamMemberRepository.save(member);
//                    });
//        }
//        return findActiveByUserId(userId); // Return updated list
//    }

    // Search functionality
    public List<TeamMember> searchTeamMembers(UUID userId, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return findActiveByUserId(userId);
        }

        String searchLower = searchTerm.toLowerCase();
        List<TeamMember> allMembers = findActiveByUserId(userId);

        return allMembers.stream()
                .filter(member ->
                        (member.getName() != null && member.getName().toLowerCase().contains(searchLower)) ||
                                (member.getRole() != null && member.getRole().toLowerCase().contains(searchLower)) ||
                                (member.getBio() != null && member.getBio().toLowerCase().contains(searchLower)) ||
                                (member.getSkills() != null && member.getSkills().toLowerCase().contains(searchLower)) ||
                                (member.getDepartment() != null && member.getDepartment().toLowerCase().contains(searchLower)) ||
                                (member.getLocation() != null && member.getLocation().toLowerCase().contains(searchLower))
                )
                .toList();
    }
}