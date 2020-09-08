package cn.bucheng.miaosha.controller;

import cn.bucheng.miaosha.model.vo.ItemVO;
import cn.bucheng.miaosha.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yinchong
 * @create 2020/9/7 19:16
 * @description
 */
@RestController
@RequestMapping("item")
public class ItemController extends BaseController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("init")
    public ItemVO init(Long id){
        return itemService.initItem(id);
    }
}
