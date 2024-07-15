package com.cydeo.service.impl;

import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.exception.UserNotFoundException;
import com.cydeo.mapper.MapperUtil;
import com.cydeo.repository.UserRepository;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final MapperUtil mapper;

    public UserServiceImpl(UserRepository userRepository, @Lazy ProjectService projectService, @Lazy TaskService taskService, MapperUtil mapper) {
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.taskService = taskService;
        this.mapper = mapper;
    }

    @Override
    public List<UserDTO> listAllUsers() {
        return userRepository.findAllByIsDeletedOrderByFirstNameDesc(false).stream()
                .map(user -> mapper.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findByUserName(String username) {
        User foundUser = userRepository.findByUserNameAndIsDeleted(username, false);
        if (foundUser == null) {
            throw new UserNotFoundException("User " + username + " does not exists.");
        }
        return mapper.convert(foundUser, new UserDTO());
    }

    @Override
    public void save(UserDTO user) {
        userRepository.save(mapper.convert(user, new User()));
    }

    @Override
    public UserDTO update(UserDTO dto) {
        //find current user from db
        User entity = userRepository.findByUserNameAndIsDeleted(dto.getUserName(), false); // has id
        User convertedDto = mapper.convert(dto, new User());
        convertedDto.setId(entity.getId());
        userRepository.save(convertedDto);
        return findByUserName(dto.getUserName());
    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUserNameAndIsDeleted(username, false);
        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }
    }

    @Override
    public List<UserDTO> listAllByRole(String role) {
        return userRepository.findAllByRoleDescriptionIgnoreCaseAndIsDeleted(role, false).stream()
                .map(user -> mapper.convert(user, new UserDTO()))
                .collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user) {

        return switch (user.getRole().getDescription()) {
            case "Manager" -> {
                List<ProjectDTO> projectDTOList = projectService.listAllNonCompletedByAssignedManager(mapper.convert(user, new UserDTO()));
                yield projectDTOList.isEmpty();
            }
            case "Employee" -> {
                List<TaskDTO> taskDTOList = taskService.listAllNonCompletedByAssignedEmployee(mapper.convert(user, new UserDTO()));
                yield taskDTOList.isEmpty();
            }
            default -> true;
        };
    }

}
