package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    // 목록/검색 예시: /question/list?page=0&kw=spring
    @GetMapping("/question/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw",   defaultValue = "") String kw) {

        // 운영(prod)에서는 파일로, 개발(dev)에서는 콘솔로 출력되도록 설정(프로퍼티)과 짝을 맞춥니다.
        log.debug("GET /question/list page={}, kw='{}'", page, kw);
        log.info("List requested: page={}, kw='{}'", page, kw);

        Page<Question> paging = questionService.getList(page, kw); // 기존 서비스 메서드 사용
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list"; // 뷰 템플릿은 기존 것 사용
    }
}
