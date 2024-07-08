package my.practice.jpashop.api;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Member;
import my.practice.jpashop.service.MemberService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController // @Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {
    private final MemberService memberService;

    // 문제점 - API가 엔티티에 직접 의존
    // 엔티티가 외부로 노출될 경우 엔티티 변경으로 인해 API에 의존하는 시스템에도 변화가 필요해짐
    // 정보 은닉이 이뤄지지 않아 변화 파급력 증가

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        // member 연관정보 노출 -> jackson(JSON 라이브러리) @JsonIgnore 활용
        // 엔티티에 불필요한 로직(presentation 관련 로직) 추가는 좋지 않다
        // List형태의 JSON을 사용하는 것도 바람직하지 않음 -> JSON 형식 확장이 어려워짐(ex. 데이터 관련 메타 속성 추가)

        return memberService.findMembers();
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // @RequestBody -> 메시지 body를 엔티티에 맵핑
        // @Valid -> 클래스에 정의된 validation 진행
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
