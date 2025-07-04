package com.hnz.enums;

/**
 * @Desc: 延迟市场枚举
 */
public enum DelayTimes {

    first(1, 3 * 1000),
    second(2, 5 * 1000),
    third(3, 8 * 1000),
    fourth(4, 10 * 1000),
    fifth(5, 15 * 1000),
    sixth(6, 20 * 1000),
    seventh(7, 25 * 1000),
    eighth(8, 30 * 1000),
    ninth(9, 40 * 1000);

    public final Integer counts;
    public final Integer times;

    DelayTimes(Integer counts, Integer times) {
        this.counts = counts;
        this.times = times;
    }

    public static Integer getDelayTimes(Integer counts) {
        DelayTimes[] delayTimes = DelayTimes.values();
        for (DelayTimes dt :delayTimes) {
            if (dt.counts == counts) {
                return dt.times;
            }
        }
        // 如果没有匹配的，则使用默认的5分钟
        return delayTimes[delayTimes.length-1].times;
    }
}
