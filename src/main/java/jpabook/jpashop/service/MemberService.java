package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //데이터 변경하는 것은 transaction 있어야함, readOnly = true 읽기 전용
@RequiredArgsConstructor // autowired memberRepository 주입, 생성자 injection
public class MemberService {

    private final MemberRepository memberRepository;

   /**
     * 회원 가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    } //실무에서는 멀티스레드 등으로 변수 존재, 최후의 방어 필요, 데이터 멤버 이름 unique 하게 잡기

    /**
     * 회원 전체 조회
     * @return
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 단 건 조회
     * @param memberId
     * @return
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 회원 이름 수정 (변경 감지)
     * @param id
     * @param name
     */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
