package my.practice.jpashop.controller;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원 이름은 필수입니다.") // 값이 비어있을 경우 오류 발생, 사용처에서 @Valid를 이용해 validation 진행
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
