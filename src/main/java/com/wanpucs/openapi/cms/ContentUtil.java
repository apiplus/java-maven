package com.wanpucs.openapi.cms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wanpucs.openapi.exception.OpenApiException;
import com.wanpucs.openapi.sdk.OpenApi;

/*
 * 封装内容操作的SDK工具类
 */
public class ContentUtil extends OpenApiUtils {

	public static class PagePojo implements Serializable {

		private static final long serialVersionUID = -3017403058988477031L;
		private String id;
		private String region_id;
		private String category_id;
		private String title;
		private String alias;
		private String summary;
		private Map<String, Object> metas;
		private Map<String, Object> attributes;
		private List<String> pics;
		private String content;
		private String status;
		private String order_by;
		private String account_id;
		private String gmt_created;
		private String gmt_modified;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRegion_id() {
			return region_id;
		}

		public void setRegion_id(String region_id) {
			this.region_id = region_id;
		}

		public String getCategory_id() {
			return category_id;
		}

		public void setCategory_id(String category_id) {
			this.category_id = category_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getSummary() {
			return summary;
		}

		public void setSummary(String summary) {
			this.summary = summary;
		}

		public Map<String, Object> getMetas() {
			return metas;
		}

		public void setMetas(Map<String, Object> metas) {
			this.metas = metas;
		}

		public Map<String, Object> getAttributes() {
			return attributes;
		}

		public void setAttributes(Map<String, Object> attributes) {
			this.attributes = attributes;
		}

		public List<String> getPics() {
			return pics;
		}

		public void setPics(List<String> pics) {
			this.pics = pics;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getOrder_by() {
			return order_by;
		}

		public void setOrder_by(String order_by) {
			this.order_by = order_by;
		}

		public String getAccount_id() {
			return account_id;
		}

		public void setAccount_id(String account_id) {
			this.account_id = account_id;
		}

		public String getGmt_created() {
			return gmt_created;
		}

		public void setGmt_created(String gmt_created) {
			this.gmt_created = gmt_created;
		}

		public String getGmt_modified() {
			return gmt_modified;
		}

		public void setGmt_modified(String gmt_modified) {
			this.gmt_modified = gmt_modified;
		}

	}

	public static class PageList implements Serializable {

		private static final long serialVersionUID = 8481249429346459719L;
		private Integer page;
		private Integer perpage;
		private Integer total;
		private Integer offset;
		private List<PagePojo> items;

		public Integer getPage() {
			return page;
		}

		public void setPage(Integer page) {
			this.page = page;
		}

		public Integer getPerpage() {
			return perpage;
		}

		public void setPerpage(Integer perpage) {
			this.perpage = perpage;
		}

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public Integer getOffset() {
			return offset;
		}

		public void setOffset(Integer offset) {
			this.offset = offset;
		}

		public List<PagePojo> getItems() {
			return items;
		}

		public void setItems(List<PagePojo> items) {
			this.items = items;
		}
	}

	public static Map<String, Object> getPageById(OpenApi sdk, Integer pageId)
			throws OpenApiException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("id", pageId);
		Map<String, Object> pageMap = (Map<String, Object>) api(sdk,
				"wanpu.page_get", paramMap, Map.class);
		return pageMap;
	}

	public static PageList getPageListByCategoryId(OpenApi sdk,
			Integer catagoryId, Integer page, Integer pageSize)
			throws OpenApiException {
		if (page == null) {
			page = 1;
		}

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("selection", "category_id=?");
		paramMap.put("page", page);
		List<Integer> paramList = new ArrayList<Integer>();
		paramList.add(catagoryId);
		paramMap.put("selection", "category_id=?");
		if (pageSize != null) {
			paramMap.put("perpage", pageSize);
		}
		paramMap.put("selection_args", paramList);
		paramMap.put("order_by", "order_by asc, id desc");
		PageList pageList = (PageList) api(sdk, "wanpu.page_query", paramMap,
				PageList.class);

		return pageList;

	}

	public static String loadTaskJsonForTest(String path) {
		File file = new File(path);
		String result = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
			String s = null;
			while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
				result = result + "\n" + s;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void main(String[] args) {
		// category_id = "";

		String appkey = "CugxLRt8X9604C9q";

		String secretKey = "7LRveUPWhxTjFCWiztUPKptfHsM3wD9x";

		String serverName = "http://118.178.167.238";

		OpenApi sdk;
		try {
			sdk = new OpenApi(appkey, secretKey, serverName, "http");
			sdk.setServerName(serverName);
			Map<String, Object> result = getPageById(sdk, 230);
			System.out.println(1);
			// PageList pageList = getPageListByCategoryId(sdk, 70, null, null);

		} catch (OpenApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
