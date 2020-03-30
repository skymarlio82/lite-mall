
package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.WxOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.linlinjava.litemall.wx.util.GenerateQrCodeUtil;
import org.linlinjava.litemall.wx.util.wxpay.MyWxPayConfig;
import org.linlinjava.litemall.wx.util.wxpay.WXPay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/wx/order")
@Validated
public class WxOrderController {

	@Autowired
	private WxOrderService wxOrderService;

	/**
	 * 订单列表
	 *
	 * @param userId   用户ID
	 * @param showType 订单信息
	 * @param page     分页页数
	 * @param limit    分页大小
	 * @return 订单列表
	 */
	@GetMapping("list")
	public Object list(@LoginUser Integer userId,
		@RequestParam(defaultValue="0") Integer showType,
		@RequestParam(defaultValue="1") Integer page,
		@RequestParam(defaultValue="10") Integer limit,
		@Sort @RequestParam(defaultValue="add_time") String sort,
		@Order @RequestParam(defaultValue="desc") String order) {
		return wxOrderService.list(userId, showType, page, limit, sort, order);
	}

	/**
	 * 订单详情
	 *
	 * @param userId  用户ID
	 * @param orderId 订单ID
	 * @return 订单详情
	 */
	@GetMapping("detail")
	public Object detail(@LoginUser Integer userId, @NotNull Integer orderId) {
		return wxOrderService.detail(userId, orderId);
	}

	/**
	 * 提交订单
	 *
	 * @param userId 用户ID
	 * @param body   订单信息
	 *               {cartId：xxx, addressId: xxx, couponId: xxx, message: xxx, grouponRulesId: xxx, grouponLinkId: xxx}
	 * @return 提交订单操作结果
	 */
	@PostMapping("submit")
	public Object submit(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.submit(userId, body);
	}

	/**
	 * 取消订单
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 取消订单操作结果
	 */
	@PostMapping("cancel")
	public Object cancel(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.cancel(userId, body);
	}

	/**
	 * 付款订单的预支付会话标识
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 支付订单ID
	 */
	@PostMapping("prepay")
	public Object prepay(@LoginUser Integer userId, @RequestBody String body, HttpServletRequest request) {
		return wxOrderService.prepay(userId, body, request);
	}

	/**
	 * 微信付款成功或失败回调接口
	 * <p>
	 * 注意，这里pay-notify是示例地址，建议开发者应该设立一个隐蔽的回调地址
	 *
	 * @param request  请求内容
	 * @param response 响应内容
	 * @return 操作结果
	 */
	@PostMapping("pay-notify")
	public Object payNotify(HttpServletRequest request, HttpServletResponse response) {
		return wxOrderService.payNotify(request, response);
	}

	/**
	 * 订单申请退款
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 订单退款操作结果
	 */
	@PostMapping("refund")
	public Object refund(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.refund(userId, body);
	}

	/**
	 * 确认收货
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 订单操作结果
	 */
	@PostMapping("confirm")
	public Object confirm(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.confirm(userId, body);
	}

	/**
	 * 删除订单
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 订单操作结果
	 */
	@PostMapping("delete")
	public Object delete(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.delete(userId, body);
	}

	/**
	 * 待评价订单商品信息
	 *
	 * @param userId  用户ID
	 * @param orderId 订单ID
	 * @param goodsId 商品ID
	 * @return 待评价订单商品信息
	 */
	@GetMapping("goods")
	public Object goods(@LoginUser Integer userId, @NotNull Integer orderId, @NotNull Integer goodsId) {
		return wxOrderService.goods(userId, orderId, goodsId);
	}

	/**
	 * 评价订单商品
	 *
	 * @param userId 用户ID
	 * @param body   订单信息，{ orderId：xxx }
	 * @return 订单操作结果
	 */
	@PostMapping("comment")
	public Object comment(@LoginUser Integer userId, @RequestBody String body) {
		return wxOrderService.comment(userId, body);
	}

	@Autowired
	private LitemallOrderService orderService;

	@GetMapping("wxqrcode")
	public Object applyQrCode(@NotNull Integer orderId) throws Exception {
		MyWxPayConfig config = new MyWxPayConfig();
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("body", "Jitao微信支付测试[上海行信科技]");
		LitemallOrder order = orderService.findById(orderId);
		data.put("out_trade_no", order.getOrderSn());
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", "1");
		data.put("spbill_create_ip", "47.94.142.113");
		data.put("notify_url", "https://www.sky888.cn/api/wx/order/pay-notify");
		data.put("trade_type", "NATIVE"); // 此处指定为扫码支付
		data.put("product_id", "10");
		Map<String, String> resp = new HashMap<String, String>();
		try {
			resp = wxpay.unifiedOrder(data);
//			resp.put("nonce_str", "KjbDflKver0A2rm5");
//			resp.put("code_url", "weixin://wxpay/bizpayurl?pr=mQ3y1IE");
//			resp.put("appid", "wxba3211abca2a188c");
//			resp.put("sign", "31A92628991748F33AB0C6D7AEC51B5CFBD6F5BD070A89F1943147B35173CFA9");
//			resp.put("trade_type", "NATIVE");
//			resp.put("return_msg", "OK");
//			resp.put("result_code", "SUCCESS");
//			resp.put("mch_id", "1544571151");
//			resp.put("return_code", "SUCCESS");
//			resp.put("prepay_id", "wx3018001460125962633b2ac21091018200");
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtil.ok(resp);	
	}

	@GetMapping("qrc")
	public void wsqrcode(@RequestParam(required=true, name="code_url") String code_url, HttpServletResponse resp) {
		GenerateQrCodeUtil.encodeQrcode(code_url, resp);
	}

	@GetMapping("wxpayresult")
	public Object checkWxPayResult(@NotNull Integer orderId) throws Exception {
		MyWxPayConfig config = new MyWxPayConfig();
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		LitemallOrder order = orderService.findById(orderId);
		data.put("out_trade_no", order.getOrderSn());
		Map<String, String> resp = new HashMap<String, String>();
		try {
			resp = wxpay.orderQuery(data);
//			resp.put("transaction_id", "4200000571202003304359500140");
//			resp.put("nonce_str", "4L8NV5d83QMuMKwc");
//			resp.put("trade_state", "success");
//			resp.put("bank_type", "OTHERS");
//			resp.put("openid", "oDLo50iZuc9r6p9-O3_vr3Nyi0x0");
//			resp.put("sign", "DC583EF14FBF62D34C9109405195AF672828F1249067A585247543DCE331C42C");
//			resp.put("return_msg", "OK");
//			resp.put("fee_type", "CNY");
//			resp.put("mch_id", "1544571151");
//			resp.put("cash_fee", "1");
//			resp.put("out_trade_no", "2020033016510000000001");
//			resp.put("cash_fee_type", "CNY");
//			resp.put("appid", "wxba3211abca2a188c");
//			resp.put("total_fee", "1");
//			resp.put("trade_state_desc", "支付成功");
//			resp.put("trade_type", "NATIVE");
//			resp.put("result_code", "SUCCESS");
//			resp.put("attach", "");
//			resp.put("time_end", "20200330183424");
//			resp.put("is_subscribe", "Y");
//			resp.put("return_code", "SUCCESS");
			System.out.println(resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseUtil.ok(resp);	
	}
}