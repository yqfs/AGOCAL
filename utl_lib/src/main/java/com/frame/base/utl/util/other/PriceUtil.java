package com.frame.base.utl.util.other;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PriceUtil {

    private static PriceUtil instance;
    public static final String RMB = "￥";

    private NumberFormat formater;

    private PriceUtil() {
    }

    public static PriceUtil getInstance() {
        if (instance == null) {
            instance = new PriceUtil();
            NumberFormat formater = DecimalFormat.getInstance();
            formater.setMaximumFractionDigits(2);
            formater.setMinimumFractionDigits(2);
            instance.setFormater(formater);
        }
        return instance;
    }

    /**
     * 获取展示价格（保留小数点后两位返回字符串）
     *
     * @param price
     * @return
     */
    public String getDisplayPrice(double price) {
        return formater.format(price);
    }

    public String getDisplayPriceWithRMB(double price) {
        return RMB + formater.format(price);
    }

    /**
     * 获取展示价格（保留小数点后两位返回）
     *
     * @param price
     * @return
     */
    public String getDisplayPrice(String price) {
        String displayPrice = price;
        double priceDouble = Double.parseDouble(price);
        if (priceDouble > 0) {
            displayPrice = getDisplayPrice(priceDouble);
        }
        return displayPrice;
    }

    public void setFormater(NumberFormat formater) {
        this.formater = formater;
    }
}
