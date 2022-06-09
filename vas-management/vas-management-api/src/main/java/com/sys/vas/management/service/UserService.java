package com.sys.vas.management.service;

import com.sys.vas.management.dto.entity.UserEntity;
import com.sys.vas.management.dto.entity.UserRoleEntity;
import com.sys.vas.management.exception.ApiException;
import com.sys.vas.management.dto.ResponseCodes;
import com.sys.vas.management.dto.AuthDataDto;
import com.sys.vas.management.dto.request.UserCredentialsRequestDto;
import com.sys.vas.management.repository.UserRepository;
import com.sys.vas.management.repository.UserRoleRepository;
import com.sys.vas.management.util.ApiUtil;
import com.sys.vas.management.util.JwtUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@Data
public class UserService {

    private UserRepository userRepository;
    private UserRoleRepository userRoleRepository;
    private JwtUtil jwtUtil;

    public UserService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * generate access token using given details
     * @param username
     * @return
     * @throws JoseException
     */
    public AuthDataDto createAccessToken(String username) throws JoseException {
        UserEntity userEntity = getUserRepository().findByUsername(username)
                .orElseThrow(() -> new ApiException(ResponseCodes.USER_NOT_FOUND, username + " is not found"));
        String token = getJwtUtil().generateToken(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getUserRole().getRole()
        );
        return AuthDataDto.builder()
                .id(userEntity.getId())
                .createdDate(userEntity.getCreatedDate())
                .username(userEntity.getUsername())
                .isAvtive(userEntity.getEnabled())
                .scope(userEntity.getUserRole().getRole())
                .email(userEntity.getEmail())
                .name(userEntity.getName())
                .token(token)
                .build();
    }

    /**
     * returns user details for given user id,
     * if the userid does not belong to the user who authenticated then throws an exception
     * @param userId
     * @return
     */
    public UserEntity getUserById(int userId) {
        UserEntity user = getUserRepository().findById(userId).<ApiException>orElseThrow(() ->
            new ApiException(ResponseCodes.USER_NOT_FOUND, null)
        );

        if (Objects.equals(user.getUsername(), ApiUtil.getAuthUserName())) {
            return user;
        } else {
            throw new ApiException(ResponseCodes.USER_NOT_FOUND, null);
        }
    }

    /**
     * activates or deactivates a user.
     * admin can de-activate any user
     * job-seeker cannot actdeact anyone including them selves
     * agents can actdeact only job-seekers, not admins or same user
     * @param userId
     * @return
     */
    @Transactional
    public boolean actDeatUser(int userId) {
        String role = ApiUtil.getAuthentication().getAuthorities().stream().findFirst().get().getAuthority();
        UserEntity userEntity = getUserRepository().findById(userId)
                .orElseThrow(() -> new ApiException(ResponseCodes.USER_NOT_FOUND, "user not found for id: " + userId));
        boolean b = !userEntity.getEnabled();
        log.info("status value to be updated:{}, user role:{}", b, role);
        log.info("target user id:{}, role:{}", userEntity.getId(), userEntity.getUserRole().getRole());
        //admin user can enable or disable any user
        getUserRepository().actDeactUser(b, userId);
        return b;
    }

    @Transactional
    public void createUser(UserCredentialsRequestDto request) {
        //check if user exists
        userRepository.findByUsername(request.getUsername())
                .ifPresent(e -> {
                    throw new ApiException(ResponseCodes.USER_ALREADY_FOUND, null);
                });

        UserRoleEntity userRole = getUserRoleRepository().findByRole(request.getRole())
                .orElseThrow(() -> new ApiException(ResponseCodes.USER_ROLE_NOT_FOUND, null));

        UserEntity userEntity = new UserEntity();
        userEntity.setEnabled(true);
        userEntity.setUsername(request.getUsername());
        userEntity.setCreatedDate(LocalDate.now());
//        userEntity.setEmail(request.getUsername());
        userEntity.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        userEntity.setUserRole(userRole);

        getUserRepository().save(userEntity);
    }

    public List<UserEntity> getAllUsers() {
        return getUserRepository().findAll();
    }

    public void delete(int id) {
        UserEntity userRole = getUserRepository().findById(id)
                .orElseThrow(() -> new ApiException(ResponseCodes.USER_NOT_FOUND, null));
        getUserRepository().delete(userRole);
    }
}
