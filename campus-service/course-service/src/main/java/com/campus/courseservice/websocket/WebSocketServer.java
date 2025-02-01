package com.campus.courseservice.websocket;

import com.campus.utils.thread.UserThreadLocalUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket服务
 */
@Component
//@ServerEndpoint("/ws/{sid}")
public class WebSocketServer extends TextWebSocketHandler {

    //存放会话对象
    private static Map<Long, WebSocketSession> sessionMap = new HashMap();

    /**
     * 群发
     *
     * @param message
     */
    public void sendToAllClient(String message) {
        System.out.println("开始发送");
        Collection<WebSocketSession> sessions = sessionMap.values();
        for (WebSocketSession session : sessions) {
            System.out.println("发送");
            try {
                //服务器向客户端发送消息
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long user = UserThreadLocalUtil.getUser();
        System.out.println("客户端：" + user + "建立连接");
//        session.sendMessage(new TextMessage("建立连接"));
        sessionMap.put(user, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long user = UserThreadLocalUtil.getUser();
        sessionMap.remove(user);
    }
}
