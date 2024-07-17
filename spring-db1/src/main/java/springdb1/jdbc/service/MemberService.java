package springdb1.jdbc.service;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import springdb1.jdbc.domain.Member;
import springdb1.jdbc.repository.MemberRepository2;

@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository2 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toMember);
        memberRepository.update(toId, toMember.getMoney() + money);
    }

    private void validation(Member toMember) {
        if(toMember.getMemberId().equals("ex")){
            throw new IllegalStateException("Not enough money");
        }
    }

}
