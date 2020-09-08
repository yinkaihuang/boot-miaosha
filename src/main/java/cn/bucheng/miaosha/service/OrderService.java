package cn.bucheng.miaosha.service;

/**
 * @author ：yinchong
 * @create ：2019/7/22 14:34
 * @description：
 * @modified By：
 * @version:
 */
public interface OrderService   {

    void createNewOrder(Long itemId, Long userId, Integer amount, Long promoId);

    void doCreateNewOrder(Long itemId, Long userId, Integer amount, Long promoId, Long stockLogId);
}
