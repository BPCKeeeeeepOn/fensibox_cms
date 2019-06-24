package com.fensibox.cms.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fensibox.cms.common.CmsConstants;
import com.fensibox.cms.common.GeneratorID;
import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.product.ProductDetailOutVM;
import com.fensibox.cms.controller.vm.product.ProductListOutVM;
import com.fensibox.cms.entity.MemberDiscount;
import com.fensibox.cms.entity.Product;
import com.fensibox.cms.entity.ProductExample;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.repository.MemberDiscountMapper;
import com.fensibox.cms.repository.ProductMapper;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.fensibox.cms.result.ProductListOut;
import com.fensibox.cms.utils.CommonUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private CmsBizMapper cmsBizMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MemberDiscountMapper memberDiscountMapper;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    final String product_list_url = "http://renqitop.00sq.cn.api.94sq.cn/api/goods/list";

    final String product_info_url = "http://renqitop.00sq.cn.api.94sq.cn/api/goods/info";


    public List<ProductListOutVM> list(PageVM pageVM, String pname) {
        List<ProductListOutVM> productListOutVMS;
        productListOutVMS = PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectProductList(pname));
        return productListOutVMS;
    }

    public Product detail(Long id) {
        return productMapper.selectByPrimaryKey(id);
    }

    public int update(Long id, Double price) {
        if (id == 0 && price == 0) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        Product product = productMapper.selectByPrimaryKey(id);
        product.setPrice(price);
        return productMapper.updateByPrimaryKeySelective(product);
    }

    public Map<String, Object> selectDiscount() {
        MemberDiscount memberDiscount = cmsBizMapper.selectDiscount();
        Double discount = memberDiscount.getDiscount();
        Map<String, Object> map = new HashMap<>();
        map.put("discount", discount);
        return map;
    }

    public int updateDiscount(Double discount) {
        if (discount == null || discount == 0) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        MemberDiscount memberDiscount = cmsBizMapper.selectDiscount();
        memberDiscount.setDiscount(discount);
        return memberDiscountMapper.updateByPrimaryKeySelective(memberDiscount);
    }

    public void syncProduct() {
        List<ProductListOut> list = null;
        //组装签名
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        Map<String, Object> params = new TreeMap<>();
        params.put("api_token", CmsConstants.tokenId);
        params.put("timestamp", timestamp);
        String sign = CommonUtils.generateSign(params);
        params.put("sign", sign);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(params);
            HttpEntity<String> request = new HttpEntity<>(json, requestHeaders);
            ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(product_list_url, request, JsonNode.class);
            JsonNode jsonNode = responseEntity.getBody();
            log.info("result jsonNode :{}", jsonNode.toString());
            String data = jsonNode.get("data").toString();
            list = objectMapper.readValue(data, new TypeReference<List<ProductListOut>>() {
            });
            if (list == null || list.size() == 0) {
                throw new BizException(ResponseResult.fail(ResultCode.DEFAULT_ERROR));
            }
            for (ProductListOut productListOut : list) {
                ProductDetailOutVM productDetailOutVM = null;
                productDetailOutVM = queryProduct(Long.valueOf(productListOut.getGid()));
                if (productDetailOutVM == null) {
                    continue;
                }
                Product product = productDetailOutVM.buildProduct();
                Long gid = product.getGid();

                ProductExample productExample = new ProductExample();
                productExample.createCriteria().andGidEqualTo(gid);
                List<Product> productList = productMapper.selectByExample(productExample);
                if (productList.size() > 0) {
                    Product updateProduct = productList.get(0);
                    updateProduct.setClose(product.getClose());
                    updateProduct.setModelId(product.getModelId());
                    updateProduct.setLimitMax(product.getLimitMax());
                    updateProduct.setLimitMin(product.getLimitMin());
                    updateProduct.setRate(product.getRate());
                    updateProduct.setpName(product.getpName());
                    updateProduct.setCid(product.getCid());
                    productMapper.updateByPrimaryKeySelective(updateProduct);
                    continue;
                }
                product.setId(GeneratorID.getId());
                productMapper.insertSelective(product);
            }
        } catch (Exception e) {
            log.error("get product info failed:", e.getMessage(), e);
            throw new BizException(ResponseResult.fail(ResultCode.PRODUCT_NOT_EXISZTS));
        }
    }

    public ProductDetailOutVM queryProduct(Long gid) {
        ProductDetailOutVM productDetailOutVM = null;
        //组装签名
        String timestamp = String.valueOf(Instant.now().getEpochSecond());
        Map<String, Object> params = new TreeMap<>();
        params.put("api_token", CmsConstants.tokenId);
        params.put("timestamp", timestamp);
        params.put("gid", String.valueOf(gid));
        String sign = CommonUtils.generateSign(params);
        params.put("sign", sign);
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        String json = null;
        try {
            json = objectMapper.writeValueAsString(params);
            HttpEntity<String> request = new HttpEntity<>(json, requestHeaders);
            ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(product_info_url, request, JsonNode.class);
            JsonNode jsonNode = responseEntity.getBody();
            log.info("result jsonNode :{}", jsonNode.toString());
            String data = jsonNode.get("data").toString();
            productDetailOutVM = objectMapper.readValue(data, ProductDetailOutVM.class);
            log.info("result productDetailOutVM :{}", productDetailOutVM.toString());
        } catch (Exception e) {
            log.error("get product info failed:", e.getMessage(), e);
        }
        return productDetailOutVM;
    }

}
