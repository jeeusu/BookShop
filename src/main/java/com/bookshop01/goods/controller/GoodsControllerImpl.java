package com.bookshop01.goods.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.bookshop01.common.base.BaseController;
import com.bookshop01.goods.service.GoodsService;
import com.bookshop01.goods.vo.GoodsVO;

import net.sf.json.JSONObject;

@Controller("goodsController")
@RequestMapping(value="/goods")
public class GoodsControllerImpl extends BaseController   implements GoodsController {
	@Autowired
	private GoodsService goodsService;
	
	private String root ="common/layout";
	
	@RequestMapping(value="/goodsDetail.do" ,method = RequestMethod.GET) // 도서 상세페이지
	public ModelAndView goodsDetail(@RequestParam("goods_id") String goods_id,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName=(String)request.getAttribute("viewName");
		HttpSession session=request.getSession();
		Map goodsMap=goodsService.goodsDetail(goods_id);
		ModelAndView mav = new ModelAndView(root);
		mav.addObject("goodsMap", goodsMap);
		mav.addObject("body", viewName);
		GoodsVO goodsVO=(GoodsVO)goodsMap.get("goodsVO");
		
		addGoodsInQuick(goods_id,goodsVO,session);
		
		return mav;
	}
	
	@RequestMapping(value="/keywordSearch.do",method = RequestMethod.GET,produces = "application/text; charset=utf8")
	public @ResponseBody String  keywordSearch(@RequestParam("keyword") String keyword, 
			                                  HttpServletRequest request, HttpServletResponse response) throws Exception{ // 도서 검색 시 검색어 목록
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		//System.out.println(keyword);
		if(keyword == null || keyword.equals(""))
		   return null ;
		
		keyword = keyword.toUpperCase();
		keyword="%"+keyword+"%";
		
	    List<String> keywordList =goodsService.keywordSearch(keyword);
	    
	 // 검색 결과를 기반으로 JSONObject 생성
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("keyword", keywordList);
		 		
	    String jsonInfo = jsonObject.toString();
	   // System.out.println(jsonInfo);
	    return jsonInfo ;
	}
	
	@RequestMapping(value="/searchGoods.do" ,method = RequestMethod.GET)
	public ModelAndView searchGoods(@RequestParam("searchWord") String searchWord,
			                       HttpServletRequest request, HttpServletResponse response) throws Exception{ // 도서 검색
		String viewName=(String)request.getAttribute("viewName");
		
		searchWord = "%"+searchWord+"%";
		
		List<GoodsVO> goodsList=goodsService.searchGoods(searchWord);
		ModelAndView mav = new ModelAndView(root);
		mav.addObject("body", viewName);
		mav.addObject("goodsList", goodsList);
		return mav;
		
	}
	
	private void addGoodsInQuick(String goods_id, GoodsVO goodsVO, HttpSession session) { //최근 본 목록
	    boolean already_existed = false;
	    
	    // 최근 본 상품 목록 ArrayList
	    List<GoodsVO> quickGoodsList;
	    quickGoodsList = (ArrayList<GoodsVO>) session.getAttribute("quickGoodsList");

	    if (quickGoodsList != null) {
	        if (quickGoodsList.size() < 4) { 
	            // 이미 본 상품 목록에 동일한 상품이 있는지 확인
	            for (int i = 0; i < quickGoodsList.size(); i++) {
	                GoodsVO _goodsBean = (GoodsVO) quickGoodsList.get(i);
	                if (goods_id.equals(_goodsBean.getGoods_id())) {
	                    already_existed = true;
	                    break;
	                }
	            }
	            // 동일한 상품이 없다면 목록에 추가
	            if (!already_existed) {
	                quickGoodsList.add(goodsVO);
	            }
	        }
	    } else {
	        // 세션에 quickGoodsList가 없을 경우 새로 생성
	        quickGoodsList = new ArrayList<GoodsVO>();
	        quickGoodsList.add(goodsVO);
	    }

	    // 세션에 목록과 목록 개수를 저장
	    session.setAttribute("quickGoodsList", quickGoodsList);
	    session.setAttribute("quickGoodsListNum", quickGoodsList.size());
	}
	
	@Override
	@RequestMapping(value="/goodsList.do", method=RequestMethod.GET)
	public ModelAndView goodsList(@RequestParam("goods_sort") String goodsSort,
	                              HttpServletRequest request, HttpServletResponse response) throws Exception {
	    
	    List<GoodsVO> goodsList = goodsService.goodsList(goodsSort);

	    ModelAndView mav = new ModelAndView(root);
	    mav.addObject("body", "/goods/goodsList");// 경로와 파일명 맞게 수정
	    mav.addObject("goodsList", goodsList);
	    mav.addObject("goods_sort", goodsSort);
	    return mav;
	}

}
