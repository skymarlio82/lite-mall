
package org.linlinjava.litemall.wx.config;

import java.util.HashMap;
import java.util.Map;

import org.linlinjava.litemall.db.domain.LitemallOrder;
import org.linlinjava.litemall.db.service.LitemallOrderService;
import org.linlinjava.litemall.wx.model.wxpay.MyWxPayConfig;
import org.linlinjava.litemall.wx.model.wxpay.WXPay;
import org.springframework.beans.factory.annotation.Autowired;

public class MyWxPayService {

	@Autowired
	private LitemallOrderService orderService = null;

	private MyWxPayProperties props = null;

	public MyWxPayService(MyWxPayProperties props) {
		this.props = props;
	}

	public Map<String, String> applyWxQrCode(Integer orderId, String body, Integer totalFee) throws Exception {
		MyWxPayConfig config = new MyWxPayConfig(props);
		WXPay wxpay = new WXPay(config);
		Map<String, String> data = new HashMap<String, String>();
		data.put("body", body);
		LitemallOrder order = orderService.findById(orderId);
		data.put("out_trade_no", order.getOrderSn());
		data.put("device_info", "");
		data.put("fee_type", "CNY");
		data.put("total_fee", String.valueOf(totalFee));
		data.put("spbill_create_ip", props.getIpaddress());
		data.put("notify_url", props.getNotifyurl());
		// 此处指定为扫码支付
		data.put("trade_type", "NATIVE");
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
			System.out.println("wxpay.unifiedOrder >> " + resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	public Map<String, String> checkWxPayment(Integer orderId) throws Exception {
		MyWxPayConfig config = new MyWxPayConfig(props);
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
			System.out.println("wxpay.orderQuery >> " + resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}
}