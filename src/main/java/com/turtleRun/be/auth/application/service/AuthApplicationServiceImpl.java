package com.turtleRun.be.auth.application.service;

import com.turtleRun.be.auth.application.dto.*;
import com.turtleRun.be.auth.domain.entity.User;
import com.turtleRun.be.auth.domain.service.AuthenticationDomainService;
import com.turtleRun.be.auth.domain.valueobject.*;
import com.turtleRun.be.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 인증 애플리케이션 서비스 구현체
 * 도메인 서비스들을 조합하여 애플리케이션의 유스케이스를 처리합니다.
 */
@Service
@Transactional
public class AuthApplicationServiceImpl implements AuthApplicationService {
    
    private final AuthenticationDomainService authenticationDomainService;
    private final UserRepository userRepository;
    private final JwtTokenService jwtTokenService;
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    public AuthApplicationServiceImpl(AuthenticationDomainService authenticationDomainService,
                                    UserRepository userRepository,
                                    JwtTokenService jwtTokenService) {
        this.authenticationDomainService = authenticationDomainService;
        this.userRepository = userRepository;
        this.jwtTokenService = jwtTokenService;
    }
    
    @Override
    @Transactional
    public SignUpResponseDto signUp(SignUpRequestDto request) {
        try {
            // Value Objects 생성
            Name name = Name.of(request.getName());
            Email email = Email.of(request.getEmail());
            Username username = Username.of(request.getUsername());
            Password password = Password.of(request.getPassword());
            
            // 도메인 Entity 생성
            User user = new User(name, email, username, password);
            
            // 도메인 서비스를 통한 회원가입 처리
            User savedUser = authenticationDomainService.register(user);
            
            // 응답 DTO 생성
            UserInfoDto userInfo = createUserInfoDto(savedUser);
            return SignUpResponseDto.success(userInfo);
            
        } catch (IllegalArgumentException e) {
            return SignUpResponseDto.failure(e.getMessage());
        } catch (Exception e) {
            return SignUpResponseDto.failure("회원가입 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto request) {
        try {
            // 사용자 조회 (사용자명 또는 이메일로)
            Optional<User> userOpt = findUserByUsernameOrEmail(request.getUsernameOrEmail());
            
            if (userOpt.isEmpty()) {
                return LoginResponseDto.failure("사용자를 찾을 수 없습니다");
            }
            
            User user = userOpt.get();
            
            // 인증 처리
            boolean isAuthenticated = authenticationDomainService.authenticate(user, request.getPassword());
            
            if (!isAuthenticated) {
                return LoginResponseDto.failure("비밀번호가 올바르지 않습니다");
            }
            
            // 마지막 로그인 시간 업데이트
            user.updateLastLogin();
            userRepository.save(user);
            
            // JWT 토큰 생성
            String accessToken = jwtTokenService.generateToken(user);
            long expiresIn = jwtTokenService.getExpirationTime();
            
            // 응답 DTO 생성
            UserInfoDto userInfo = createUserInfoDto(user);
            return LoginResponseDto.success(accessToken, expiresIn, userInfo);
            
        } catch (Exception e) {
            return LoginResponseDto.failure("로그인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public SignUpResponseDto getUserInfo(String userId) {
        try {
            UserId domainUserId = UserId.of(java.util.UUID.fromString(userId));
            Optional<User> userOpt = userRepository.findById(domainUserId);
            
            if (userOpt.isEmpty()) {
                return SignUpResponseDto.failure("사용자를 찾을 수 없습니다");
            }
            
            User user = userOpt.get();
            UserInfoDto userInfo = createUserInfoDto(user);
            
            return new SignUpResponseDto(true, "사용자 정보 조회 성공", userInfo);
            
        } catch (Exception e) {
            return SignUpResponseDto.failure("사용자 정보 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
    
    /**
     * 사용자명 또는 이메일로 사용자를 조회합니다.
     * 
     * @param usernameOrEmail 사용자명 또는 이메일
     * @return 사용자 (Optional)
     */
    private Optional<User> findUserByUsernameOrEmail(String usernameOrEmail) {
        if (EMAIL_PATTERN.matcher(usernameOrEmail).matches()) {
            // 이메일 형식인 경우
            Email email = Email.of(usernameOrEmail);
            return userRepository.findByEmail(email);
        } else {
            // 사용자명인 경우
            Username username = Username.of(usernameOrEmail);
            return userRepository.findByUsername(username);
        }
    }
    
    /**
     * 도메인 User Entity를 UserInfoDto로 변환합니다.
     * 
     * @param user 도메인 User Entity
     * @return UserInfoDto
     */
    private UserInfoDto createUserInfoDto(User user) {
        return new UserInfoDto(
            user.getId().getValue().toString(),
            user.getName().getValue(),
            user.getEmail().getValue(),
            user.getUsername().getValue(),
            user.getStatus(),
            user.getEmailVerificationStatus(),
            user.getLastLoginAt(),
            user.getCreatedAt(),
            user.getUpdatedAt()
        );
    }
}
