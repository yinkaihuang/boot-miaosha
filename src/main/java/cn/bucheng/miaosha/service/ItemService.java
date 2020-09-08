package cn.bucheng.miaosha.service;

import cn.bucheng.miaosha.model.po.ItemPO;
import cn.bucheng.miaosha.model.vo.ItemVO;

import java.util.List;


public interface ItemService  {
    List<ItemPO> listItems();

    ItemVO initItem(Long id);
}
