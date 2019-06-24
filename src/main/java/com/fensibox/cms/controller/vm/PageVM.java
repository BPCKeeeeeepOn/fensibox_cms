package com.fensibox.cms.controller.vm;

import lombok.Data;

import static com.fensibox.cms.common.CmsConstants.DEFAULT_PAGE_OFFSET;
import static com.fensibox.cms.common.CmsConstants.DEFAULT_PAGE_SIZE;

@Data
public class PageVM {


    private int limit = DEFAULT_PAGE_SIZE;


    private int offset = DEFAULT_PAGE_OFFSET;

}
