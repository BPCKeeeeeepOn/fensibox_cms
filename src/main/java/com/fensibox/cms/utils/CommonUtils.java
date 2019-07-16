package com.fensibox.cms.utils;

import com.fensibox.cms.common.CmsConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class CommonUtils {


    public static int generateWorkerId() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            String hostname = address.getHostAddress();
            if (StringUtils.isNotBlank(hostname)) {
                String bits = new BigInteger(hostname.getBytes()).toString(2);
                BigInteger rightPart = new BigInteger(StringUtils.right(bits, 10), 2);
                return rightPart.intValue();
            }
        } catch (UnknownHostException e) {
            log.error("unknown host, Reason: ", e);
        }
        return 0;
    }

    public static String generateSign(Map<String, Object> params) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String key : params.keySet()) {
            if (key != null && params.get(key) != null) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    builder.append("&");
                }
                builder.append(key)
                        .append("=")
                        .append(params.get(key));
            }
        }
        System.out.println(builder.toString());
        return DigestUtils.md5Hex(builder.toString() + CmsConstants.secret);
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return map;
    }

    /**
     * 随机生成6位数
     * @return
     */
    public static String randomNum() {
        int num = (int) ((Math.random() * 9 + 1) * 100000);
        return String.valueOf(num);
    }

    /**
     * @param number 需要保留两位的数
     * @return
     */
    public static double doubleFormat(double number) {
        // 将double类型转为BigDecimal
        BigDecimal bigDecimal = new BigDecimal(number);
        // 保留两位小数,并且四舍五入
        return bigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @param number 需要保留五位的数
     * @return
     */
    public static double doubleFormat5(double number) {
        // 将double类型转为BigDecimal
        BigDecimal bigDecimal = new BigDecimal(number);
        // 保留两位小数,并且四舍五入
        return bigDecimal.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
