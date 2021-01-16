package cn.quguai.lockzk.controller;


import cn.quguai.lockzk.api.BaseResponse;
import cn.quguai.lockzk.api.StatusCode;
import cn.quguai.lockzk.service.UserRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegController {

    @Autowired
    private UserRegService userRegService;

    @GetMapping("/user/reg/submit")
    public BaseResponse<String> reg(String username, String password) {
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)) {
            return new BaseResponse<>(StatusCode.InvalidParams);
        }
        BaseResponse<String> response = new BaseResponse<>(StatusCode.Success);
        try {
//            userRegService.userRegNoLock(username, password);
            userRegService.userRegWithZKLock(username, password);
        }catch (Exception e){
            response = new BaseResponse<>(StatusCode.Fail.getCode(), e.getMessage());
        }
        return response;
    }
}
