package yjkmust.com.jayfun.Bean;

/**
 * Created by GEOFLY on 2017/8/17.
 */

public class ZhiHuStoryBean {
    private String Date;
    private ZhiHuDateBean.StoriesBean bean;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public ZhiHuDateBean.StoriesBean getBean() {
        return bean;
    }

    public void setBean(ZhiHuDateBean.StoriesBean bean) {
        this.bean = bean;
    }
}
