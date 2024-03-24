package com.hzvtc.myproject.socket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzvtc.myproject.dto.Message;
import com.hzvtc.myproject.entity.SystemUser;
import com.hzvtc.myproject.exception.HttpException;
import com.hzvtc.myproject.service.SystemUserService;
import com.hzvtc.myproject.utils.HttpCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 熊新欣
 * @date 2021-01-02
 * 参考：https://blog.csdn.net/weixin_38111957/article/details/86352677
 */
@Component
@ServerEndpoint("/ws/{name}")
public class WebSocket {

    private Session session;

    private String name;

    public final static Map<String,WebSocket> WEB_SOCKET_SET = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,@PathParam(value = "name") String name){
        this.session = session;
        this.name = name;
        WEB_SOCKET_SET.put(name,this);
    }

    @OnClose
    public void onClose(){
        WEB_SOCKET_SET.remove(name);
    }

    @OnMessage
    public void onMessage(String message) throws JsonProcessingException {
        Message m = new ObjectMapper().readValue(message, Message.class);
        System.out.println(m);
    }

    /**
     * 发送消息
     * @param userId 目标用户id
     * @param message 消息内容
     * @param systemUserService 。
     */
    public static boolean sendMessage(Long userId, Message message, SystemUserService systemUserService) {
        SystemUser systemUser = systemUserService.get(userId).orElseThrow(() -> new HttpException(HttpCode.FAILED, "用户不存在"));
        if (WEB_SOCKET_SET.containsKey(systemUser.getLoginName())) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                WEB_SOCKET_SET.get(systemUser.getLoginName()).session.getBasicRemote()
                        .sendText(objectMapper.writeValueAsString(message));
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
