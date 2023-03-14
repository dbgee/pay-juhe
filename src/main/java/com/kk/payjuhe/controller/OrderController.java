package com.kk.payjuhe.controller;

import com.kk.payjuhe.utils.AlipayUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class OrderController {

    @RequestMapping("/order")
    @ResponseBody
    public String order(Model model, @RequestParam String id ){
        String form=AlipayUtils.getOrderForm(id);
        model.addAttribute("form",form);
        log.debug("form:{}",form);
        return form;
    }

    @RequestMapping("/")
    public String index(){
        return "index";
    }

    @RequestMapping("/query")
    @ResponseBody
    public String query(@RequestParam String id){
        return AlipayUtils.queryOrderStatus(id);
    }
}
