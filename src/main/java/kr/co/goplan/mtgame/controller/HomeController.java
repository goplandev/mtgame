package kr.co.goplan.mtgame.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /*@RequestMapping("/denied")
    public String denied(Model model, Authentication auth, HttpServletRequest req){
        AccessDeniedException ade = (AccessDeniedException) req.getAttribute(WebAttributes.ACCESS_DENIED_403);
        model.addAttribute("auth", auth);
        model.addAttribute("errMsg", ade);
        return "denied";
    }*/
}
