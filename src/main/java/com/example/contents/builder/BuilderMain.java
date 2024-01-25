package com.example.contents.builder;

public class BuilderMain {
    public static void main(String[] args) {
        // User에는 총 8개의 필드가 있고,
        // 전체 필드를 받아서 생성해주는 생성자가 있을 떄,
        // username, email, firstName, lastName 만 넣어서 초기화를 하고 싶다면
        // edujeeho, edujeeho@gmail.com, Jeeho, Park
       /* User newUser1 = new User(
                null,
                "edujeeho",
                null,
                "edujeeho@gmail.com",
                null,
                "Jeeho",
                "Park",
                null
        );
*/
        // javaBean
        /*User newUser2 = new User();
        newUser2.setUsername("edujeeho");
        newUser2.setEmail("edujeeho@gmail.com");*/
        // ...

        //User.UserBuilder userBuilder = new User.UserBuilder();
        // UserBuilder
        /*User.UserBuilder a = userBuilder.id(1L);
        User.UserBuilder b = a.username("edujeeho");
        User.UserBuilder c = b.email("edujeeho@gmail.com");
*/
        User newUser = User.builder()
                .username("edujeeho")
                .email("edujeeho@gmail.com")
                .firstName("Jeeho")
                .lastName("Park")
                .build();


    }
}