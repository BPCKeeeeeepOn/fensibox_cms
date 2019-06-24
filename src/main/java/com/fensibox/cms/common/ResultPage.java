package com.fensibox.cms.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ResultPage {
	/**
	 * 当前页
	 */
	@JsonProperty("cur_page")
	private Integer curPage;
	/**
	 * 每页多少
	 */
	@JsonProperty("page_size")
	private Integer pageSize;
	/**
	 * 总页数
	 */
	@JsonProperty("total_page")
	private Integer totalPage;

	/**
	 * 总条数
	 */
	@JsonProperty("total_row")
	private Long totalRow;
	/**
	 * 排序
	 */
	@JsonProperty("order_by")
	private String orderBy;


	public ResultPage() {
		super();
	}

    /**
     *
     * @param curPage
     *            当前页
     * @param pageSize
     *            每页多少
     * @param totalPage
     *            总页数
     * @param totalRow
     *            总条数
     */
	public ResultPage(Integer curPage, Integer pageSize, Integer totalPage, Long totalRow, String orderBy) {
		super();
		this.curPage = curPage;
		this.pageSize = pageSize;
		this.totalPage = totalPage;
		this.totalRow = totalRow;
		this.orderBy = orderBy;
	}

}
