package com.example.contents;

import com.example.contents.dto.UserDto;
import com.example.contents.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

// 다른것들은 잘 동작한다고 가정
@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Mock
    // 실제 UserRepository가 아나리
    // UserRepository의 메서드를 다 가지고 있지만
    // 동작은 다르게 하는 Mock 객체
    private UserRepository userRepository;

    @InjectMocks
    // 실제 UserRepository가 아닌
    // 위에 만든 짝퉁 userRepository를 의존성으로 사용
    private UserService userService;

    // UserDto를 인자로 받아 User를 생성하고
    // 그 결과를 UserDto로 반환
    @Test
    @DisplayName("UserDto로 사용자 생성")
    public void testCreateUser() {
        // given
        // 1. userRepository가 특정 Use를 전달받을 것을 가정한다.
        String username = "edujeeho";
        // userRepository가 입력받을 user
        User userIn = new User(username, null, null, null);
        // 2. userRepository가 반환할 user
        User userOut = new User(username, null, null, null);
//        userOut.setId(1L); // 잠깐 User의 id에 @Setter 붙임

        // 3. userRepository.save(userIn)의 결과를 userOut으로 설정
        when(userRepository.save(userIn))
                .thenReturn(userOut);
        when(userRepository.existsByUsername(username))
                .thenReturn(false);

        // when - userDto를 전달한다.
        UserDto userDto = new UserDto(
                null,
                username,
                null,
                null,
                null,
                null
        );
        UserDto result = userService.create(userDto);

        // then - 돌아온 result를 검사한다.
        assertEquals(username, result.getUsername());
    }

    // readUserByUsername
    @Test
    @DisplayName("username으로 읽기")
    public void testReadUsername(){
        // given
        String username = "subin";
        User userOut = new User(username, null, null, null);

        // userRepository.findByUsername(username)의 결과를 userOut으로 설정
        when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(userOut));

        // when - username으로 사용자를 읽는다.
        UserDto result = userService.readUserByUsername(username);

        // then - 돌아온 result를 검사한다.
        assertEquals(username, result.getUsername());
    }

    // UserDto를 이용해 User 수정
    @Test
    @DisplayName("UserDto를 이용해 User 수정")
    public void testUpdateUser() {
        // given
        Long userId = 1L;
        String username = "subin";
        String bio = "Developer Developing Developers";
        User  user = new User(username, null,null,null);

        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));

        doAnswer(returnsFirstArg())
                .when(userRepository)
                .save(any());

        // when
        UserDto userDto = new UserDto(userId,username, null,null, bio, null);
        // updateUser에서 오류 발생
        UserDto result = userService.updateUser(userId, userDto);

        // then
        assertEquals(userId, result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(bio, result.getBio());
    }

    @Test
    @DisplayName("User에 이미지 추가")
    public void testUpdateUserAvatar() {
        // given
        Long userId = 1L;
        String username = "subin";
        User user = new User(username, null,null,null);
        when(userRepository.findByUsername(user.getUsername()))
                .thenReturn(Optional.of(user));
        doAnswer(returnsFirstArg())
                .when(userRepository)
                .save(any());

        // when
        MultipartFile mockFile = new MockMultipartFile(
                "image",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "test".getBytes()
        );
        // updateUserAvatar 에서 오류
        UserDto userDto = userService.updateUserAvatar(userId, mockFile);

        // then
        assertEquals(userId, userDto.getId());
        assertEquals(String.format("/static/%d/profile.png", userId), userDto.getAvatar());
    }


}
