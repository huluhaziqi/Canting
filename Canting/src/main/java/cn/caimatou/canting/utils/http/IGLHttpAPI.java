package cn.caimatou.canting.utils.http;

import java.util.LinkedHashMap;

/**
 * Description：Http api interface
 * <br/><br/>Created by Fausgoal on 15/8/27.
 * <br/><br/>
 */
public interface IGLHttpAPI {
    void login(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void logout(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void step1(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void step2(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void step3(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void fetchSuppliers(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void updatePassword(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void sendCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void loginByCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void updatePasswordByCheckCode(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void updateUserInfo(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void restaurant(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void addressAdd(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void addressEdit(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void setAddressDefault(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void addressRemove(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void addressCompList(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void hisOrders(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void myOrders(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void confirmDelivery(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void orderCancel(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void orderInfo(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void orderSubmit(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void fetchMySuppliers(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);

    void addToMySupplier(String funcKey, LinkedHashMap<String, Object> mapParams, GLApiHandler handler);
}
