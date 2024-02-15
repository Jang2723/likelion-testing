package com.example.contents;

import com.example.contents.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// UserRepository의 단위 테스트를 위한 테스트들
@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    // 사용자를 추가하는 테스트
    @Test
    @DisplayName("새로운 User 추가")
    public void testCreateUser() {
        // 테스트의 가독성을 높이는 패턴
        // given - 테스트를 진행하기 위한 기본 조건을 만들어 두는 부분
        // 내가 만들고자 하는 User 엔티티가 있는 상황에서
        String username = "subin";
        User user = new User(username, null, null, null);

        // when  - 실제로 테스트를 진행하는 부분
        // userRepository.save(user)를 진행한다.
        User result = userRepository.save(user);

        // then  - 내가 기대한대로 동작했는지 검증
        // userRepository.save()의 결과의 username이 본래 User의 username과 일치했는지
        assertEquals(username, result.getUsername());
        // userRepository.save()의 결과의 id가 null이 아닌지
        assertNotNull(result.getId());
    }

    // 사용자를 추가하는데 실패하는 테스트
    // 두명의 사용자는 username이 겹치면 안되니까
    // 하나의 username을 가진 사람이 있다고 가정하고
    // 같은 username을 사용해서 User를 생성할때는 실패해야 한다.
    @Test
    @DisplayName("새로운 User 추가 실패(중복 username)")
    public void testCreateUserFail(){
        // given - 어떤 특정 username을 가진 User가 이미 저장된 상황에서
        String username = "subin";
        User userGiven = new User(username, null, null, null);
        userRepository.save(userGiven);

        // when - 동일한 username을 가진 User를 저장하려고 하면
        User newUser = new User(username, null, null, null);

        // then - 실패한다.
        assertThrows(Exception.class, () -> userRepository.save(newUser));
    }

    // 사용자를 조회하는 테스트
    @Test
    @DisplayName("username으로 존재하는 사용자 조회")
    public void testReadUser(){
        // given
        // 내가 읽고자 하는 특정 User가 데이터베이스에 저장이 된 이후의 상황에서
        String username = "subin";
        User userGiven = new User(username, null, null, null);
        userRepository.save(userGiven);

        // when
        // 해당하는 username을 가지고 userRepository.findByUsername(username);의 결과를 받아오면
        Optional<User> optionalUser = userRepository.findByUsername(username);

        // then
        // 돌아온 결과 Optional.isPresent() == true이고, (assertTrue)
        assertTrue(optionalUser.isPresent());
        // 돌아온 결과 Optional.get().getUsername == username이다.
        assertEquals(username, optionalUser.get().getUsername());
    }


    // 존재하지 않는 username을 가지고 조회하면 Optional.empty()가 반환된다.
    @Test
    @DisplayName("username으로 user 찾기 실패")
    public void testReadUserFail() {
        // given
        String username = "subin";
        User userGiven = new User(username, null,null,null);
        userRepository.save(userGiven);

        // when
        Optional<User> optionalUser = userRepository.findByUsername("not_found");

        // then
        assertTrue(optionalUser.isEmpty());

    }

    // 이미 존재하는 username을 검색해서 존재하는지 확인한다.
    @Test
    @DisplayName("username으로 user 존재 유무 판단")
    public void testExistsByUsername() {
        // given
        String usernameExists = "subin";
        String usernameExistsNot = "not_found";
        User userGiven = new User(usernameExists,null,null,null);
        userRepository.save(userGiven);

        // when
        // 존재하는지 찾기
        Boolean exists = userRepository.existsByUsername(usernameExists);
        Boolean existsNot = userRepository.existsByUsername(usernameExistsNot);

        // then 판단
        assertTrue(exists);
        assertFalse(existsNot);
    }

    // id를 가지고  User를 삭제한다.
    @Test
    @DisplayName("id로 user 삭제")
    public void testDeleteById() {
        // given
        String username = "subin";
        User target = new User(username,null,null,null);
        Long id = userRepository.save(target).getId();

        // when
        userRepository.deleteById(id);

        // then
        assertFalse(userRepository.existsByUsername(username));
    }
}
