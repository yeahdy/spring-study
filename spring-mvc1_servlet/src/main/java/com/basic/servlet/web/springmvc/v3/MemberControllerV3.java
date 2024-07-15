package com.basic.servlet.web.springmvc.v3;

import com.basic.servlet.domain.member.Member;
import com.basic.servlet.domain.member.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/springmvc/v3/members")
public class MemberControllerV3 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @GetMapping("/new-form")
    public String process(){
        return "new-form";
    }

    @PostMapping(value = "/save")
    public String saveMember(@RequestParam("username") String username, @RequestParam("age") int age, Model model){
        Member member = new Member(username, age);
        memberRepository.save(member);
        model.addAttribute("member",member);
        return "save-result";
    }

    @GetMapping( "/list")
    public String selectMembers(Model model){
        List<Member> members = memberRepository.findAll();
        model.addAttribute("members",members);
        return "members";
    }

}
