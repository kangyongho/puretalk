package com.puretalk.controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.puretalk.db.dao.MemberDAO;
import com.puretalk.db.dao.MessageDAO;
import com.puretalk.db.domain.ChattingList;
import com.puretalk.db.domain.FriendList;
import com.puretalk.db.domain.Message;
import com.puretalk.db.domain.Result;
import com.puretalk.db.domain.User;
import com.puretalk.db.jdomain.JsonGetUser;
import com.puretalk.db.jdomain.JsonChattingList;
import com.puretalk.db.jdomain.JsonCheckId;
import com.puretalk.db.jdomain.JsonFriendList;
import com.puretalk.db.jdomain.JsonJoin;
import com.puretalk.db.jdomain.JsonLoginResult;
import com.puretalk.db.jdomain.JsonMessage;
import com.puretalk.db.jdomain.JsonUserList;

// 컨트롤러이자 POJO 클래스
// 비지니스 로직 및 서비스 로직을 넣는다.
@Controller
public class PureTalkController {
	@Autowired
	MessageDAO messageDAO;
	@Autowired
	MemberDAO memberDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "user";
	}

	@RequestMapping(value = "checkloginid/{loginId}", method = RequestMethod.GET)
	@ResponseBody
	public Result checklogin(@PathVariable String loginId) {
		Result result = new Result();
		result.setDuplicated(true);
		result.setAvailableId(loginId + (int) (Math.random() * 1000));
		System.out.println(result);
		// 자바 빈이나 HashMap의 Map타입으로 던져주면 메시지 컨버터에 등록한
		// MappingJackson2HttpMessageConverter에 의해서 자동으로 JSON으로 만들어서 리턴한다.
		// 따라서 원하는 정보를 자바 빈 또는 Map타입으로 만들어 주기만 하면 되겠다. ^^
		return result;
	}

	@RequestMapping(value = "addmessage", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage insertMessage(@RequestBody Message messageFromAndroid) {
		String message = messageFromAndroid.getMessage();
		String usercellphone = messageFromAndroid.getUsercellphone();
		String friendcellphone = messageFromAndroid.getFriendcellphone();
		String flag = messageFromAndroid.getFlag();
		
		System.out.println(message);
		System.out.println(usercellphone);
		System.out.println(friendcellphone);
		System.out.println(flag);
		// 이름은 2차 개발
		//String toname = messageFromAndroid.getToname();
		//String fromname = messageFromAndroid.getFromname();

		messageDAO.addMessage(message, usercellphone, friendcellphone, flag);
		
		// 위에서 메시지 저장 후 메시지 리스트 조회한 결과를 돌려주는 DAO 호출
		ArrayList<Map<String, Object>> messageObject = messageDAO.listMessage(usercellphone, friendcellphone);
		JsonMessage jsonMessage = new JsonMessage();
		jsonMessage.setArrayList(messageObject);
		System.out.println("addMessage 메소드 호출 되었습니다.");
		return jsonMessage;
	}

	// 채팅 목록을 클릭했을 때 해당하는 채팅 내용을 불러오는 컨트롤러
	@RequestMapping(value = "message", method = RequestMethod.POST)
	@ResponseBody
	public JsonMessage jsonMessage(@RequestBody Message message) {
		String usercellphone = message.getUsercellphone();
		String friendcellphone = message.getFriendcellphone();
		System.out.println(usercellphone);
		System.out.println(friendcellphone);
		
		ArrayList<Map<String, Object>> messageObject = messageDAO.listMessage(usercellphone, friendcellphone);
		JsonMessage jsonMessage = new JsonMessage();
		jsonMessage.setArrayList(messageObject);
		System.out.println("message 메소드 호출 되었습니다.");
		return jsonMessage;
	}

	// 친구목록 조회 컨트롤러
	@RequestMapping(value = "friendlist", method = RequestMethod.POST)
	@ResponseBody
	public JsonFriendList jsonFriendList(@RequestBody FriendList friendList) {
		String usercellphone = friendList.getUsercellphone();
		
		ArrayList<Map<String, Object>> friendListObject = memberDAO.getFriendList(usercellphone);
		JsonFriendList jsonFriendList = new JsonFriendList();
		jsonFriendList.setArrayList(friendListObject);
		System.out.println("jsonFriendList 메소드 호출 되었습니다.");
		return jsonFriendList;
	}

	// 채팅목록 조회 컨트롤러
	@RequestMapping(value = "chattinglist", method = RequestMethod.POST)
	@ResponseBody
	public JsonChattingList jsonChattingList(@RequestBody ChattingList chattingList) {
		// JSON--->ChattingList에 저장된 usercellphone을 외래키로 채팅리스트를 조회한다.
		String usercellphone = chattingList.getUsercellphone();
		System.out.println(usercellphone);
		
		ArrayList<Map<String, Object>> chattingListObject = memberDAO.getChattingList(usercellphone);
		
		JsonChattingList jsonChattingList = new JsonChattingList();
		jsonChattingList.setArrayList(chattingListObject);
		System.out.println("jsonChattingList 메소드 호출 되었습니다.");
		return jsonChattingList;
	}

	// 아이디 중복 검사 컨트롤러
	@RequestMapping(value = "checkId/{loginId}", method = RequestMethod.GET)
	@ResponseBody
	public JsonCheckId checkId(@PathVariable String loginId) {
		System.out.println(loginId);
		Boolean checkIdResult = memberDAO.checkId(loginId);
		JsonCheckId jsonCheckId = new JsonCheckId();
		jsonCheckId.setCheckIdResult(checkIdResult);
		System.out.println("checkId 메소드 호출 되었습니다.");
		return jsonCheckId;
	}

	// 회원가입 컨트롤러
	@RequestMapping(value = "join", method = RequestMethod.POST)
	@ResponseBody
	public JsonJoin join(@RequestBody User user) {
		String id = user.getId();
		String password = user.getPassword();
		String usercellphone = user.getUsercellphone();
		int memberNumber = memberDAO.join(id, password, usercellphone);
		System.out.println(memberNumber);
		System.out.println("join 메소드 호출 되었습니다.");
		JsonJoin jsonJoin = new JsonJoin(memberNumber);
		return jsonJoin;
	}

	// 최초 가입시 친구목록 동기화를 위한 User 리스트 조회 컨트롤러
	@RequestMapping(value = "setfriendlist", method = RequestMethod.POST)
	@ResponseBody
	public JsonUserList userlist(@RequestBody JsonGetUser user) {
		// 스마트폰 주소록
		ArrayList<Map> phoneBookNumber = user.getPhoneBookNumber();
		
		// 데이터베이스 주소록
		ArrayList<Map<String, String>> userList = memberDAO.userList();

		int phonesize = phoneBookNumber.size();
		int dbsize = userList.size();

		if (phonesize < dbsize) {
			for (int i = 0; i < phonesize; i++) {
				for (int j = 0; j < dbsize; j++) {
					Map<String, String> smartMap = phoneBookNumber.get(i);
					String name = smartMap.get("name");
					String phone = smartMap.get("phone");
					String usercellphone = smartMap.get("usercellphone");
					Map<String, String> dbMap = userList.get(j);
					String dbusercellphone = dbMap.get("usercellphone");
					if (dbusercellphone.equals(phone)) {
						// 스마트폰과 서버의 전화번호가 같으므로 PureTalk 사용자이다. DB에 저장
						// 수동 동기화시 중복 저장이 될 것이다. DUPLICATE KEY UPDATE 등 방법 시도 실패
						// insert전에 기존 입력 정보의 flag를 0으로 설정해서 목록에서 조회되지 않게 하자.
						memberDAO.updateFriendList(usercellphone, dbusercellphone);
						memberDAO.updateChattingList(usercellphone, dbusercellphone);
						
						memberDAO.setFirendList(usercellphone, name, dbusercellphone, 1);
						memberDAO.setChattingList(usercellphone, name, dbusercellphone, 0);
					}
				}
			}
		} else {
			for (int i = 0; i < dbsize; i++) {
				Map<String, String> dbMap = userList.get(i);
				String dbusercellphone = dbMap.get("usercellphone");
				for (int j = 0; j < phonesize; j++) {
					Map<String, String> smartMap = phoneBookNumber.get(j);
					String name = smartMap.get("friendname");
					String phone = smartMap.get("friendphone");
					String usercellphone = smartMap.get("usercellphone");
					// 꼭 존재하는 값으로 먼저 비교한다. NullPointExceptin 방지. dbusercellphone은 반드시 있다.
					if (dbusercellphone.equals(phone)) {
						// 스마트폰과 서버의 전화번호가 같으므로 PureTalk 사용자이다. DB에 저장
						memberDAO.updateFriendList(usercellphone, dbusercellphone);
						memberDAO.updateChattingList(usercellphone, dbusercellphone);
						
						memberDAO.setFirendList(usercellphone, name, dbusercellphone, 1);
						memberDAO.setChattingList(usercellphone, name, dbusercellphone, 1);
					} else {
						//System.out.println("없는 사용자 "+j+" : "+ name +" : "+phone+" : "+usercellphone+"=====");
					}
				}
			}
		}

		JsonUserList jsonUserList = new JsonUserList();
		jsonUserList.setArrayList(userList);

		System.out.println("userlist 메소드 호출 되었습니다.");
		return jsonUserList;
	}
	
	// 로그인 컨트롤러
	@RequestMapping(value = "login", method = RequestMethod.POST)
	@ResponseBody
	public JsonLoginResult login(@RequestBody User user) {
		String id = user.getId();
		String password = user.getPassword();
		Boolean loginResult = memberDAO.loginResult(id, password);
		JsonLoginResult jsonLoginResult = new JsonLoginResult(loginResult);
		System.out.println("login 메소드 호출 되었습니다.");
		return jsonLoginResult;
	}
	
	// GCM 업데이트 컨트롤러 (안드로이드에서 회원가입 후 실행됨)
	// User, FirendList, ChattingList 세개를 업데이트 한다.
	@RequestMapping(value = "updategcm", method = RequestMethod.POST)
	@ResponseBody
	public void updateGCM(@RequestBody User user) {
		String usercellphone = user.getUsercellphone();
		System.out.println(usercellphone);
		
		String gcmId = user.getGcmId();
		System.out.println(gcmId);
		
		memberDAO.updateGCM(usercellphone, gcmId);
		System.out.println("updategcm 메소드 호출 되었습니다.");
	}
}









