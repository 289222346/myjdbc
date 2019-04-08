package entity.po;

import com.myjdbc.util.PojoBean;

import java.util.ArrayList;
import java.util.List;

public class WebViewShowBo {
    private String url;
    private String name;
    private String image;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public static WebViewShowBo init(String json) {
        WebViewShowBo bo = (WebViewShowBo) PojoBean.jsonToBo(new WebViewShowBo(), json);
        return bo;
    }

    public static WebViewShowList inits(String json) {
        WebViewShowList obj = PojoBean.jsonToBo(new WebViewShowList(), json);
        return obj;
    }

    @Override
    public String toString() {
        return "WebViewShowBo [url=" + url + ", name=" + name + ", image=" + image + "]";
    }


}
