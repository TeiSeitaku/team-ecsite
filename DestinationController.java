package jp.co.internous.sunflower.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

import jp.co.internous.sunflower.model.domain.MstDestination;
import jp.co.internous.sunflower.model.domain.MstUser;
import jp.co.internous.sunflower.model.form.DestinationForm;
import jp.co.internous.sunflower.model.mapper.MstDestinationMapper;
import jp.co.internous.sunflower.model.mapper.MstUserMapper;
import jp.co.internous.sunflower.model.session.LoginSession;
/**
 * 宛先情報に関する処理のコントローラー
 * @author インターノウス
 *
 */
@Controller
@RequestMapping("/sunflower/destination")
public class DestinationController {

	/*
	 * フィールド定義
	 */
	@Autowired
	private MstUserMapper userMapper;

	@Autowired
	private LoginSession loginSession;

	@Autowired
	private MstDestinationMapper  destinationMapper;

	private Gson gson = new Gson();


	/**
	 * 宛先画面を初期表示する
	 * @param m 画面表示用オブジェクト
	 * @return 宛先画面
	 */
	@RequestMapping("/")
	public String index(Model m) {
		MstUser user = userMapper.findByUserNameAndPassword(loginSession.getUserName(),loginSession.getPassword());

		m.addAttribute("user", user);
		// page_header.htmlでsessionの変数を表示させているため、loginSessionも画面に送る。
		m.addAttribute("loginSession", loginSession);

		return "destination";

	}

	/**
	 * 宛先情報を削除する
	 * @param destinationId 宛先情報ID
	 * @return true:削除成功、false:削除失敗
	 */
	@SuppressWarnings("unchecked")
	@PostMapping("/delete")
	@ResponseBody
	public boolean delete(@RequestBody String destinationId) {

		Map<String, String> map = gson.fromJson(destinationId, Map.class);
		String id = map.get("destinationId");

		// 論理削除する
		int result = destinationMapper.logicalDeleteById(Integer.parseInt(id));

		return result > 0;

	}

	/**
	 * 宛先情報を登録する
	 * @param f 宛先情報のフォーム
	 * @return 宛先情報id
	 */
	@PostMapping("/register")
	@ResponseBody
	public String register(@RequestBody DestinationForm f) {
			MstDestination destination = new MstDestination(f);
			int userId = loginSession.getUserId();
			destination.setUserId(userId);
			int count = destinationMapper.insert(destination);

			// 登録した宛先のIDを取得(toStringするためInteger宣言)
			Integer id = count > 0 ? destination.getId() : 0;

			return id.toString();

	}
}

