package com.jubotech.framework.util;

import java.util.HashMap;
import java.util.Map;

import com.swordintent.chatgpt.ChatgptClientImpl;
import com.swordintent.chatgpt.protocol.ChatGptConfig;
import com.swordintent.chatgpt.protocol.ChatRequest;
import com.swordintent.chatgpt.protocol.ChatResponse;

public class ChatGPTUtils {
	
	private static ChatgptClientImpl client=null;
	
	static {
		init();
	}
	
	public static void init() {
		try {
			ChatGptConfig chatGptConfig = ChatGptConfig.builder()
//	              .proxy("http://192.168.50.254:9853")
//				  .email("")
//	              .authorization("")
				  .password("")
						.build();
				client = ChatgptClientImpl.getInstance();
				client.init("http://127.0.0.1:5000", chatGptConfig);
		} catch (Exception e) {
		}
	}
	
	public static Map<String, String> askOpenAi(String msg, String conversationId){
		Map<String, String> map = new HashMap<>();
		try {
			if(null == client) {
				init();
			}

			ChatRequest chatRequest = ChatRequest.builder().prompt(msg).conversationId(conversationId).build();
			ChatResponse chat = client.chat(chatRequest);
			 
			map.put("conversationId", chat.getConversationId());
			map.put("message", chat.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;

	}
	
	
	public static void main(String[] args) {
		
		Map<String,String> map3 = ChatGPTUtils.askOpenAi("ChatGPT你能告诉我如何年入百万吗", null);
		System.out.println(map3.get("message"));
	}
}
