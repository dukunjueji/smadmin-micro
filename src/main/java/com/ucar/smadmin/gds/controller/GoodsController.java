package com.ucar.smadmin.gds.controller;

import com.ucar.smadmin.base.bd.service.MemberGradeService;
import com.ucar.smadmin.common.annotation.AccessLogin;
import com.ucar.smadmin.common.base.controller.BaseController;
import com.ucar.smadmin.common.redis.RedisComponent;
import com.ucar.smadmin.common.redis.RedissonManager;
import com.ucar.smapi.common.vo.PageVO;
import com.ucar.smapi.common.vo.Result;
import com.ucar.smadmin.enums.StokeStatusEnum;
import com.ucar.smapi.gds.dto.GoodsListDTO;
import com.ucar.smapi.gds.re.GoodsDetailRE;
import com.ucar.smapi.gds.re.GoodsRE;
import com.ucar.smapi.gds.re.HotTagRE;
import com.ucar.smadmin.gds.service.GoodsService;
import com.ucar.smadmin.gds.vo.GoodsStokeVO;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 请填写类注释
 *
 * @author zhongling(ling.zhong @ ucarinc.com)
 * @since 2018年10月16日 11:17
 */
@Controller
@RequestMapping("api/gds/goods")
public class GoodsController extends BaseController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MemberGradeService memberGradeService;
    @Autowired
    RedisComponent redis;

    /**
     * 功能描述: 获取热门推荐
     *
     * @param: []
     * @return: com.ycc.base.common.Result<java.util.List<com.uc.training.smadmin.gds.re.GoodsRE>>
     * @auther: ling
     * @date: 2018/10/17 17:17
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "getHotRecommend.do_", method = RequestMethod.GET)
    public Result<List<GoodsRE>> getHotRecommend() {
        List<GoodsRE> list = goodsService.getHotRecommend();
        return Result.getSuccessResult(list);
    }

    /**
     * 功能描述: 通过分类来获取商品
     *
     * @param: [demoListVO]
     * @return: com.ycc.base.common.Result<PageVO<com.uc.training.smadmin.gds.re.GoodsRE>>
     * @auther: ling
     * @date: 2018/10/19 9:02
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "getGoodsPageByCategory.do_", method = RequestMethod.POST)
    public Result<PageVO<GoodsRE>> getGoodsPageByCategory(GoodsListDTO goodsListDTO) {
        PageVO<GoodsRE> pageVO=goodsService.getGoodsPageByCategory(goodsListDTO);
        return Result.getSuccessResult(pageVO);
    }

    /**
     * 功能描述: 商品详情
     *
     * @param:
     * @return:
     * @auther: ling
     * @date: 2018/10/19 9:01
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "getGoodsDetailByGoodsId.do_", method = RequestMethod.GET)
    public Result<List<GoodsDetailRE>> getGoodsDetailByGoodsId(@RequestParam("goodsId") Long goodsId) {
        return Result.getSuccessResult(goodsService.getGoodsDetailByGoodsId(goodsId));
    }

    /**
     * 功能描述: 根据商品名称模糊查询商品列表
     *
     * @param:
     * @return:
     * @auther: ling
     * @date: 2018/10/22 9:49
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "searchByGoodsName.do_", method = RequestMethod.POST)
    public Result<PageVO<GoodsRE>> searchByGoodsName(GoodsListDTO goodsListDTO) {
        if(StringUtils.isEmpty(goodsListDTO.getName())) {
            return null;
        }
        PageVO<GoodsRE> pageVO = goodsService.searchByGoodsName(goodsListDTO);
        return Result.getSuccessResult(pageVO);
    }

    /**
     * 功能描述: 获取热门标签
     *
     * @param:
     * @return:
     * @auther: ling
     * @date: 2018/10/22 9:50
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "selectHotTag.do_", method = RequestMethod.GET)
    public Result<List<HotTagRE>> selectHotTag() {
        return Result.getSuccessResult(goodsService.selectHotTag());
    }

    /**
     * 功能描述: 获取会员的折扣点
     *
     * @param:
     * @return:
     * @auther: ling
     * @date: 2018/10/22 9:50
     */
    @AccessLogin(required = true)
    @ResponseBody
    @RequestMapping(value = "getMemberDiscountPoint.do_", method = RequestMethod.GET)
    public Result<Double> getMemberDiscountPoint() {
        Long uid = getUid();
        return Result.getSuccessResult(memberGradeService.getDiscountByUId(uid));
    }

    /**
     * 测试高并发下的减库存安全
     *
     * @param
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "updateAndDeductStoke.do_")
    public Result<Integer> updateAndDeductStoke() {
        GoodsStokeVO goodsAndPropertyDTO = new GoodsStokeVO();
        goodsAndPropertyDTO.setPropertyId(26L);
        goodsAndPropertyDTO.setStock(1L);
        RLock lock = RedissonManager.getInstance().getLock("26Lock",true);
        try {
            lock.lock(RedissonManager.DEFAULT_EXPIRED_TIME,RedissonManager.DEFAULT_TIME_UNIT);
            Integer status = goodsService.updateAndDeductStoke(goodsAndPropertyDTO);
            System.out.println(status + "------------------------------------");
            if(status.equals(StokeStatusEnum.SUCCESS_STATUS.getStatus())) {
                return Result.getSuccessResult(status);
            } else {
                return Result.getBusinessException("减库存失败", null);
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return null;
    }

    /**
     * 测试redis
     */
    @AccessLogin(required = false)
    @ResponseBody
    @RequestMapping(value = "testRdis.do_")
    public Result<String> testRdis() {
        redis.set("helloTesst", "world",60L,TimeUnit.SECONDS);
        System.out.println(redis.get("helloTesst").toString());
        return Result.getSuccessResult("成功");
    }

}
