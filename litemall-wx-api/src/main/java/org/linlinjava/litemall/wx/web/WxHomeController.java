
package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.core.system.SystemConfig;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.service.LitemallAdService;
import org.linlinjava.litemall.db.service.LitemallBrandService;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.db.service.LitemallCouponService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.db.service.LitemallTopicService;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.HomeCacheManager;
import org.linlinjava.litemall.wx.service.WxGrouponRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/wx/home")
@Validated
public class WxHomeController {

	@Autowired
	private LitemallAdService adService;

	@Autowired
	private LitemallGoodsService goodsService;

	@Autowired
	private LitemallBrandService brandService;

	@Autowired
	private LitemallTopicService topicService;

	@Autowired
	private LitemallCategoryService categoryService;

	@Autowired
	private WxGrouponRuleService grouponService;

	@Autowired
	private LitemallCouponService couponService;

	@GetMapping("/cache")
	public Object cache(@NotNull String key) {
		if (!key.equals("litemall_cache")) {
			return ResponseUtil.fail();
		}
		HomeCacheManager.clearAll();
		return ResponseUtil.ok("缓存已清除");
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/index")
	public Object index(@LoginUser Integer userId) {
		if (HomeCacheManager.hasData(HomeCacheManager.INDEX)) {
			return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.INDEX));
		}
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		Callable<List> bannerListCallable = () -> adService.queryIndex();
		Callable<List> channelListCallable = () -> categoryService.queryChannel();
		Callable<List> couponListCallable = (userId == null) ? () -> couponService.queryList(0, 3)
			: () -> couponService.queryAvailableList(userId, 0, 3);
		Callable<List> newGoodsListCallable = () -> goodsService.queryByNew(0, SystemConfig.getNewLimit());
		Callable<List> hotGoodsListCallable = () -> goodsService.queryByHot(0, SystemConfig.getHotLimit());
		Callable<List> brandListCallable = () -> brandService.query(0, SystemConfig.getBrandLimit());
		Callable<List> topicListCallable = () -> topicService.queryList(0, SystemConfig.getTopicLimit());
		Callable<List> grouponListCallable = () -> grouponService.queryList(0, 5);
		Callable<List> floorGoodsListCallable = this::getCategoryList;
		FutureTask<List> bannerTask = new FutureTask<>(bannerListCallable);
		FutureTask<List> channelTask = new FutureTask<>(channelListCallable);
		FutureTask<List> couponListTask = new FutureTask<>(couponListCallable);
		FutureTask<List> newGoodsListTask = new FutureTask<>(newGoodsListCallable);
		FutureTask<List> hotGoodsListTask = new FutureTask<>(hotGoodsListCallable);
		FutureTask<List> brandListTask = new FutureTask<>(brandListCallable);
		FutureTask<List> topicListTask = new FutureTask<>(topicListCallable);
		FutureTask<List> grouponListTask = new FutureTask<>(grouponListCallable);
		FutureTask<List> floorGoodsListTask = new FutureTask<>(floorGoodsListCallable);
		executorService.submit(bannerTask);
		executorService.submit(channelTask);
		executorService.submit(couponListTask);
		executorService.submit(newGoodsListTask);
		executorService.submit(hotGoodsListTask);
		executorService.submit(brandListTask);
		executorService.submit(topicListTask);
		executorService.submit(grouponListTask);
		executorService.submit(floorGoodsListTask);
		Map<String, Object> entity = new HashMap<>();
		try {
			entity.put("banner", bannerTask.get());
			entity.put("channel", channelTask.get());
			entity.put("couponList", couponListTask.get());
			entity.put("newGoodsList", newGoodsListTask.get());
			entity.put("hotGoodsList", hotGoodsListTask.get());
			entity.put("brandList", brandListTask.get());
			entity.put("topicList", topicListTask.get());
			entity.put("grouponList", grouponListTask.get());
			entity.put("floorGoodsList", floorGoodsListTask.get());
			HomeCacheManager.loadData(HomeCacheManager.INDEX, entity);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
		}
		return ResponseUtil.ok(entity);
	}

	@SuppressWarnings("rawtypes")
	private List<Map> getCategoryList() {
		List<Map> categoryList = new ArrayList<>();
		List<LitemallCategory> catL1Lst = categoryService.queryL1WithoutRecommend(0, SystemConfig.getCatlogListLimit());
		for (LitemallCategory catL1 : catL1Lst) {
			List<LitemallCategory> catL2List = categoryService.queryByPid(catL1.getId());
			List<Integer> l2List = catL2List.stream().map(catL2 -> catL2.getId()).collect(Collectors.toList());
			List<LitemallGoods> categoryGoods = (l2List.size() == 0) ? 
				new ArrayList<>() : 
				goodsService.queryByCategory(l2List, 0, SystemConfig.getCatlogMoreLimit());
			Map<String, Object> catGoods = new HashMap<>();
			catGoods.put("id", catL1.getId());
			catGoods.put("name", catL1.getName());
			catGoods.put("goodsList", categoryGoods);
			categoryList.add(catGoods);
		}
		return categoryList;
	}
}