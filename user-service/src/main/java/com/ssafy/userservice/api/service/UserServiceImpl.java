package com.ssafy.userservice.api.service;

import com.ssafy.userservice.api.request.UserUpdateDto;
import com.ssafy.userservice.api.response.TokenResponseDto;
import com.ssafy.userservice.api.response.UserInfoByTokenDto;
import com.ssafy.userservice.api.response.UserInfoResponseDto;
import com.ssafy.userservice.common.exception.UserNotFoundException;
import com.ssafy.userservice.common.util.RedisService;
import com.ssafy.userservice.common.util.TokenProvider;
import com.ssafy.userservice.db.entity.User;
import com.ssafy.userservice.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    public void updateUser(String accessToken, UserUpdateDto userUpdateDto) {
        String uid = tokenProvider.getUserId(accessToken);
        User user = userRepository.findById(Long.parseLong(uid)).orElseThrow(UserNotFoundException::new);

        user.updateUser(userUpdateDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfoResponseDto getUserInfo(String codedEmail) {
        User user = userRepository.findByCodedEmail(codedEmail).orElseThrow(UserNotFoundException::new);

        UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                .id(user.getId())
                .codedEmail(user.getCodedEmail())
                .nickname(user.getNickname())
                .introduction(user.getIntroduction())
                .build();

        return userInfoResponseDto;
    }

    @Override
    @Transactional(readOnly = true)
    public TokenResponseDto reIssue(String accessToken, String refreshToken) {
        tokenProvider.validateToken(refreshToken);
        String uid = tokenProvider.getUserId(refreshToken);
        String findRefreshToken = redisService.getValue(uid);

        if (findRefreshToken == null || !findRefreshToken.equals(refreshToken)) {
            throw new UserNotFoundException("refreshToken을 찾을 수 없습니다.");
        } else {
            return TokenResponseDto.builder()
                    .accessToken(tokenProvider.createAccessToken(uid))
                    .build();
        }
    }

    @Override
    public void logout(String accessToken) {
        String uid = tokenProvider.getUserId(accessToken);

        userRepository.findById(Long.parseLong(uid))
                .orElseThrow(() -> new UserNotFoundException("수험생 정보를 찾을 수 없습니다."));
        redisService.deleteValues(uid);
    }

    @Override
    public UserInfoByTokenDto getUserID(String accessToken) {
        String uid = tokenProvider.getUserId(accessToken);
        UserInfoByTokenDto userInfoResponseDto = UserInfoByTokenDto.builder()
                .id(Long.parseLong(uid))
                .build();

        return userInfoResponseDto;
    }

    @Override
    public UserInfoByTokenDto getUserId(String codedEmail) {
        User user = userRepository.findByCodedEmail(codedEmail).orElseThrow(UserNotFoundException::new);
        UserInfoByTokenDto userInfoResponseDto = UserInfoByTokenDto.builder()
                .id(user.getId())
                .build();
        return userInfoResponseDto;
    }
}