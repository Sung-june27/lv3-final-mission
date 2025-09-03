package finalmission.controller;

import static finalmission.member.domain.Role.USER;
import static org.hamcrest.Matchers.equalTo;

import finalmission.helper.TestHelper;
import finalmission.member.domain.Member;
import finalmission.member.domain.Role;
import finalmission.member.repository.MemberRepository;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.room.domain.ConferenceRoom;
import finalmission.room.dto.request.CreateRoomRequest;
import finalmission.room.repository.ConferenceRoomRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConferenceRoomControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ConferenceRoomRepository conferenceRoomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    private Member member;
    private Member admin;
    private ConferenceRoom conferenceRoom;

    @BeforeEach
    public void setup() {
        initData();
        RestAssured.port = port;
    }

    @DisplayName("회의실을 생성할 수 없다.")
    @Test
    void createConferenceRoom() {
        // given
        String token = TestHelper.login(admin.getEmail(), admin.getPassword());

        CreateRoomRequest request = new CreateRoomRequest("회의실");

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/room")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("회의실"));
    }

    @DisplayName("어드민이 아니라면, 회의실을 생성할 수 없다.")
    @Test
    void createConferenceRoom_WhenNotAdmin() {
        // given
        String token = TestHelper.login(member.getEmail(), member.getPassword());

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .when()
                .post("/room")
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("회의실을 삭제할 수 있다.")
    @Test
    void deleteConferenceRoomByIdById() {
        // given
        String token = TestHelper.login(admin.getEmail(), admin.getPassword());

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .when()
                .delete("/room/" + conferenceRoom.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("어드민이 아니라면, 회의실을 삭제할 수 없다.")
    @Test
    void deleteConferenceRoomByIdById_WhenNotAdmin() {
        // given
        String token = TestHelper.login(member.getEmail(), member.getPassword());

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .when()
                .delete("/room/" + conferenceRoom.getId())
                .then()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    private void initData() {
        reservationRepository.deleteAll();
        memberRepository.deleteAll();
        conferenceRoomRepository.deleteAll();

        Member beforeSaveMember = new Member("사용자", "user@email.com", "password", USER);
        member = memberRepository.save(beforeSaveMember);
        Member beforeSaveAdmin =  new Member("관리자", "admin@email.com", "password", Role.ADMIN);
        admin = memberRepository.save(beforeSaveAdmin);
        ConferenceRoom beforeSaveRoom = new ConferenceRoom("회의실");
        conferenceRoom = conferenceRoomRepository.save(beforeSaveRoom);
    }
}
