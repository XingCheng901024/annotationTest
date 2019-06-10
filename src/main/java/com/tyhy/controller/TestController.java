package com.tyhy.controller;

import com.tyhy.annotation.SystemControllerLog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Administrator on 2019/6/10.
 */
@Controller
public class TestController {

    @RequestMapping(value="/say",method= RequestMethod.GET)
    @ResponseBody
    public String say(){
        return "hello world";
    }

    @RequestMapping(value="/hello",method= RequestMethod.GET)
    @ResponseBody
    @SystemControllerLog(title="哈喽模块",action="say哈喽",author = "xingcheng")
    public String hello() throws InterruptedException {
        //Thread.sleep(3000);
        return "哈喽";
    }

}
