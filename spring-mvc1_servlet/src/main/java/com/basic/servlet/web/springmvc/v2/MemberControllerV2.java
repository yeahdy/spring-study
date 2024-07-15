package com.basic.servlet.web.springmvc.v2;

import com.basic.servlet.domain.member.Member;
import com.basic.servlet.domain.member.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/springmvc/v2/members")
public class MemberControllerV2 {

    private final MemberRepository memberRepository = MemberRepository.getInstance();

    @RequestMapping("/new-form")
    public ModelAndView process(){
        return new ModelAndView("new-form");
    }

    @RequestMapping("/save")
    public ModelAndView saveMember(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelAndView modelAndView = new ModelAndView("save-result");
        modelAndView.addObject("member",member);

        return modelAndView;
    }

    @RequestMapping("/list")
    public ModelAndView selectMembers(){
        List<Member> members = memberRepository.findAll();
        ModelAndView modelAndView = new ModelAndView("members");
        modelAndView.addObject("members",members);

        return modelAndView;
    }

}
