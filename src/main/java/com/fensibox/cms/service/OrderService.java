package com.fensibox.cms.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fensibox.cms.common.CmsConstants;
import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.controller.vm.order.OrderOutVM;
import com.fensibox.cms.controller.vm.user.ClientUserOutVM;
import com.fensibox.cms.entity.ClientUser;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.entity.Order;
import com.fensibox.cms.entity.OrderExample;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.repository.OrderMapper;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.fensibox.cms.result.QueryOrderOut;
import com.fensibox.cms.utils.CommonUtils;
import com.fensibox.cms.utils.CurrentUserUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class OrderService {


    @Autowired
    private CurrentUserUtils currentUserUtils;

    @Autowired
    private CmsBizMapper cmsBizMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    final String order_url = "http://renqitop.00sq.cn.api.94sq.cn/api/order";

    final String query_order_url = "http://renqitop.00sq.cn.api.94sq.cn/api/order/query";

    public List<OrderOutVM> list(OrderContentVM orderContentVM, PageVM pageVM) {
        //获取当前用户信息
        CustomUser user = currentUserUtils.getCurrUser();
        if (user == null) {
            throw new BizException(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN));
        }
        Long uid = user.getId();
        Integer role = user.getRole();
        List<OrderOutVM> list;
        list = PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectOrderList(orderContentVM, role, uid));
        return list;
    }

    public OrderOutVM detail(Long id) {
        return cmsBizMapper.selectOrderDetail(id);
    }

    public QueryOrderOut queryOrder(Integer orderId) {
        QueryOrderOut orderOut = null;
        //查询订单
        Map<String, Object> params = new TreeMap<>();
        params.put("api_token", CmsConstants.tokenId);
        params.put("timestamp", String.valueOf(Instant.now().getEpochSecond()));
        params.put("id", orderId);
        String sign = CommonUtils.generateSign(params);
        params.put("sign", sign);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(params);
            HttpEntity<String> request = new HttpEntity<>(json, requestHeaders);
            ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(query_order_url, request, JsonNode.class);
            JsonNode jsonNode = responseEntity.getBody();
            log.info("result jsonNode :{}", jsonNode.toString());
            String data = jsonNode.get("data").toString();
            orderOut = objectMapper.readValue(data, QueryOrderOut.class);
        } catch (Exception e) {
            log.error("get product info failed:", e.getMessage(), e);
            throw new BizException(ResponseResult.fail(ResultCode.DEFAULT_ERROR));
        }
        return orderOut;
    }

    /**
     * 同步订单
     */
    public void syncOrder() {
        //获取当前用户信息
        CustomUser user = currentUserUtils.getCurrUser();
        if (user == null) {
            throw new BizException(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN));
        }
        Long uid = user.getId();
        Integer role = user.getRole();
        //查询该管理员/商务下的所有用户id
        List<ClientUserOutVM> userList = cmsBizMapper.selectClientUserList(uid, role);
        if (userList.size() == 0) {
            throw new BizException(ResponseResult.fail(ResultCode.DEFAULT_ERROR));
        }
        List<Long> ids = new ArrayList<>();
        for (ClientUserOutVM clientUser : userList) {
            Long userId = Long.valueOf(clientUser.getId());
            ids.add(userId);
        }
        //查询该用户下的所有订单
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUidIn(ids);
        List<Order> list = orderMapper.selectByExample(orderExample);
        //遍历查询订单记录，并更新
        for (Order order : list) {
            Integer oid = order.getOid().intValue();
            QueryOrderOut queryOrderOut = queryOrder(oid);
            order.setStatus(queryOrderOut.getStatus());
            order.setTnum(queryOrderOut.getTnum());
            order.setStartNum(queryOrderOut.getStartNum());
            order.setNowNum(queryOrderOut.getNowNum());
            order.setNum(queryOrderOut.getNum());
            order.setRemark(queryOrderOut.getRemark());
            orderMapper.updateByPrimaryKeySelective(order);
        }
    }


}
