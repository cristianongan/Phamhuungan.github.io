/**
 * Copyright (C) 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.evotek.qlns.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author os_linhlh2
 */
public class BigDecimalUtil {

    /**
     * Hàm cộng 2 số kiểu double
     * @param x Số thứ nhất
     * @param y Số thứ hai
     * @return Tổng 2 số
     */
    public static double add(double x, double y) {
        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));
        BigDecimal yBigDecimal = new BigDecimal(String.valueOf(y));

        BigDecimal resultBigDecimal = xBigDecimal.add(yBigDecimal);

        return resultBigDecimal.doubleValue();
    }

    /**
     * Hàm chia 2 số kiểu double
     * @param x Số thứ nhất
     * @param y Số thứ 2
     * @param scale Số chữ số phần thập phân
     * @param roundingMode Nguyên tắc làm tròn
     * @see RoundingMode
     * @since 
     * <ul type="square">
     * <li><b>RoundingMode.UP:</b> Làm tròn lên theo giá trị tuyệt đối (2.5->3, 1.1->2, -1.1->-2)</li>
     * <li><b>RoundingMode.DOWN:</b> Làm tròn xuống theo giá trị tuyệt đối(2.5->2, 1.1->1, -1.1->-1)</li>
     * <li><b>RoundingMode.CEILING:</b> Làm tròn lên theo giá trị thực (2.5->3, 1.1->2, -1.1->-1)</li>
     * <li><b>RoundingMode.FLOOR:</b> Làm tròn xuống theo giá trị thực (2.5->2, 1.1->1, -1.1->-2)</li>
     * <li><b>RoundingMode.HALF_UP:</b> Làm tròn từ 0.5 trở lên (2.5->3, 1.1->1, -1.5->-2, 2.6->3)</li>
     * <li><b>RoundingMode.HALF_DOWN:</b> Làm tròn từ 0.5 trở xuống (2.5->2, 1.1->1, -1.5->-1, 2.6->3)</li>
     * <li><b>RoundingMode.HALF_EVEN:</b> Làm tròn từ 0.5 trở lên với phần nguyên lẻ,
     * làm tròn từ 0.5 trở xuống với phần nguyên chẵn(2.5->2, 1.6->2, -1.6->-2)</li>
     * </ul>
     */
    public static double divide(
            double x, double y, int scale, RoundingMode roundingMode) {

        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));
        BigDecimal yBigDecimal = new BigDecimal(String.valueOf(y));

        BigDecimal resultBigDecimal = xBigDecimal.divide(
                yBigDecimal, scale, roundingMode);

        return resultBigDecimal.doubleValue();
    }

    public static double divide(
            int x, int y, int scale, RoundingMode roundingMode) {

        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));
        BigDecimal yBigDecimal = new BigDecimal(String.valueOf(y));

        BigDecimal resultBigDecimal = xBigDecimal.divide(
                yBigDecimal, scale, roundingMode);

        return resultBigDecimal.doubleValue();
    }

    public static double multiply(double x, double y) {
        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));
        BigDecimal yBigDecimal = new BigDecimal(String.valueOf(y));

        BigDecimal resultBigDecimal = xBigDecimal.multiply(yBigDecimal);

        return resultBigDecimal.doubleValue();
    }

    public static double scale(double x, int scale, RoundingMode roundingMode) {
        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));

        xBigDecimal.setScale(scale, roundingMode);

        return xBigDecimal.doubleValue();
    }

    public static double subtract(double x, double y) {
        BigDecimal xBigDecimal = new BigDecimal(String.valueOf(x));
        BigDecimal yBigDecimal = new BigDecimal(String.valueOf(y));

        BigDecimal resultBigDecimal = xBigDecimal.subtract(yBigDecimal);

        return resultBigDecimal.doubleValue();
    }
}
