package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.Project;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import com.cydeo.exception.ProjectAlreadyExistException;
import com.cydeo.exception.ProjectNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.ProjectRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final MapperUtil mapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, UserService userService, TaskService taskService, MapperUtil mapper) {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll(Sort.by("projectCode")).stream()
                .map(project -> mapper.convert(project, new ProjectDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        Project foundProject = projectRepository.findByProjectCode(code);
        if (foundProject == null){
            throw new ProjectNotFoundException("Project " + code + " does not exists.");
        }
        return mapper.convert(foundProject, new ProjectDTO());
    }

    @Override
    public void save(ProjectDTO dto) {
        if (projectRepository.findByProjectCode(dto.getProjectCode()) != null){
            throw new ProjectAlreadyExistException("Project " + dto.getProjectCode() + " already exists.");
        }
        dto.setProjectStatus(Status.OPEN);
        projectRepository.save(mapper.convert(dto, new Project()));
    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedDto = mapper.convert(dto, new Project());
        convertedDto.setId(project.getId());
        convertedDto.setProjectStatus(Status.OPEN);
        projectRepository.save(convertedDto);
    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setIsDeleted(true);
        //to be able to set same project code to new project
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());
        projectRepository.save(project);
        taskService.deleteByProject(mapper.convert(project, new ProjectDTO()));
    }

    @Override
    public void complete(String code) {
        Project project = projectRepository.findByProjectCode(code);
        project.setProjectStatus(Status.COMPLETE);
        projectRepository.save(project);
        taskService.completeByProject(mapper.convert(project, new ProjectDTO()));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        UserDTO currentUserDTO = userService.findByUserName(username);
        User user = mapper.convert(currentUserDTO, new User());
        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {
            ProjectDTO dto = mapper.convert(project, new ProjectDTO());
            dto.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            dto.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> listAllNonCompletedByAssignedManager(UserDTO assignedManager) {
        List<Project> projects = projectRepository
                .findAllByProjectStatusIsNotAndAssignedManager(Status.COMPLETE, mapper.convert(assignedManager, new User()));
        return projects.stream().map(project -> mapper.convert(project, new ProjectDTO())).collect(Collectors.toList());
    }
}
