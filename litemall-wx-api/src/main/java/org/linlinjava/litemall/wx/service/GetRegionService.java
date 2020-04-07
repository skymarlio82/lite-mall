
package org.linlinjava.litemall.wx.service;

import org.linlinjava.litemall.db.domain.LitemallRegion;
import org.linlinjava.litemall.db.service.LitemallRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetRegionService {

	@Autowired
	private LitemallRegionService regionService;

	private static List<LitemallRegion> litemallRegions;

	protected List<LitemallRegion> getLitemallRegions() {
		if (litemallRegions == null) {
			createRegion();
		}
		return litemallRegions;
	}

	private synchronized void createRegion() {
		if (litemallRegions == null) {
			litemallRegions = regionService.getAll();
		}
	}
}