package my.practice.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model) {
        // 모델 설정
        model.addAttribute("data", "hello!!!");

        return "hello"; // 화면 이름, "/resource/templates/hello.html" 지칭
    }
}
