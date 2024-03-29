package com.ucar.smadmin.gds.controller;

import com.ucar.smadmin.common.annotation.AccessLogin;
import com.ucar.smapi.common.vo.Result;
import com.ucar.smapi.gds.re.HotTagRE;
import com.ucar.smadmin.gds.service.HotTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 版权声明： Copyright (c) 2008 ucarinc. All Rights Reserved.
 *
 * @author 何麒（qi.he@ucarinc.com）
 * @Version 1.0
 * @date 2018/10/24
 */
@Controller
@RequestMapping("/api/gds/hotTag")
public class HotTagController {
    @Autowired
    private HotTagService hotTagService;

    /**
     * 获取热门标签
     * @return
     */
    @ResponseBody
    @AccessLogin(required = false)
    @RequestMapping(value = "selectHotTag.do_",method = RequestMethod.GET)
    public Result<List<HotTagRE>> selectHotTag() {
        Result<List<HotTagRE>> result = Result.getSuccessResult(hotTagService.selectHotTag());
        return result;
    }
}
