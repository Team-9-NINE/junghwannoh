package com.example.demo;

import com.example.demo.Question;
import com.example.demo.QuestionService;
import com.example.demo.SiteUser;
import com.example.demo.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final QuestionService questionService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/create/{qid}")
    public String create(@PathVariable("qid") Long questionId,
                         @Valid AnswerForm form, BindingResult br,
                         Principal principal, Model model) {
        Question q = questionService.get(questionId);
        if (br.hasErrors()) { model.addAttribute("question", q); return "question_detail"; }
        SiteUser author = userService.getUser(principal.getName());
        answerService.create(q, form.getContent(), author);
        return "redirect:/question/detail/" + questionId;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/answer/modify/{id}")
    public String modifyForm(@PathVariable Long id, AnswerForm form, Principal principal, Model model) {
        Answer a = answerService.get(id);
        if (!a.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        form.setContent(a.getContent());
        model.addAttribute("answer", a);
        return "answer_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/answer/modify/{id}")
    public String modify(@PathVariable Long id, @Valid AnswerForm form, BindingResult br, Principal principal) {
        if (br.hasErrors()) return "answer_form";
        Answer a = answerService.get(id);
        if (!a.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다.");
        answerService.modify(a, form.getContent());
        return "redirect:/question/detail/" + a.getQuestion().getId();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/answer/delete/{id}")
    public String delete(@PathVariable Long id, Principal principal) {
        Answer a = answerService.get(id);
        if (!a.getAuthor().getUsername().equals(principal.getName()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
        Long qid = a.getQuestion().getId();
        answerService.delete(a);
        return "redirect:/question/detail/" + qid;
    }
}
