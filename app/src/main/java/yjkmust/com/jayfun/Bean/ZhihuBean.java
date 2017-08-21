package yjkmust.com.jayfun.Bean;

import java.util.List;

/**
 * Created by GEOFLY on 2017/8/16.
 */

public class ZhihuBean {

    /**
     * date : 20170816
     * stories : [{"images":["https://pic2.zhimg.com/v2-80b94dbf7d5523580e4400a282e0faf5.jpg"],"type":0,"id":9567157,"ga_prefix":"081609","title":"「真正牛掰的人都很低调」，背后的逻辑是什么？"},{"title":"现在回头看，才更加感叹「中国队长」蔡赟是怎样的传奇（多图）","ga_prefix":"081608","images":["https://pic1.zhimg.com/v2-1e27189ebfa310ea629e8ad495ae78e4.jpg"],"multipic":true,"type":0,"id":9572874},{"images":["https://pic4.zhimg.com/v2-a34e2e56e50fd6f29f681a807fde8f6f.jpg"],"type":0,"id":9572871,"ga_prefix":"081607","title":"明明昨天出了一身汗，这个 T 恤闻起来还能接着穿"},{"title":"除了小黄片，VR 技术并不适合拍电影（多图）","ga_prefix":"081607","images":["https://pic2.zhimg.com/v2-05c2d1c8be979c467c5ab4563b73f34d.jpg"],"multipic":true,"type":0,"id":9572197},{"images":["https://pic3.zhimg.com/v2-7588659fd974d2766b3568060e24a142.jpg"],"type":0,"id":9572605,"ga_prefix":"081607","title":"南京南站涉嫌猥亵女童男子被抓，就算亲哥也难逃追责"},{"images":["https://pic1.zhimg.com/v2-86ba39ac4475a622367fe6f4d8aa7e20.jpg"],"type":0,"id":9570975,"ga_prefix":"081606","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic2.zhimg.com/v2-b146b4bcddd4a51ac53b356e0de5dd6d.jpg","type":0,"id":9572874,"ga_prefix":"081608","title":"现在回头看，才更加感叹「中国队长」蔡赟是怎样的传奇"},{"image":"https://pic1.zhimg.com/v2-a5807b177f02f8b5c3da8db97ad47878.jpg","type":0,"id":9572871,"ga_prefix":"081607","title":"明明昨天出了一身汗，这个 T 恤闻起来还能接着穿"},{"image":"https://pic3.zhimg.com/v2-28a7a811cab8e2672ed9038fe3235cce.jpg","type":0,"id":9572605,"ga_prefix":"081607","title":"南京南站涉嫌猥亵女童男子被抓，就算亲哥也难逃追责"},{"image":"https://pic4.zhimg.com/v2-ba0012eebfae7d51e9a199191918629b.jpg","type":0,"id":9572302,"ga_prefix":"081519","title":"哆啦 A 梦开心地说：「日本战败了」"},{"image":"https://pic2.zhimg.com/v2-4bd5eeb0154a34baca9c91f5b47ef8a1.jpg","type":0,"id":9571760,"ga_prefix":"081516","title":"被老师扇了耳光，学生当场猛扇回去该不该？"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic2.zhimg.com/v2-80b94dbf7d5523580e4400a282e0faf5.jpg"]
         * type : 0
         * id : 9567157
         * ga_prefix : 081609
         * title : 「真正牛掰的人都很低调」，背后的逻辑是什么？
         * multipic : true
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private boolean multipic;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isMultipic() {
            return multipic;
        }

        public void setMultipic(boolean multipic) {
            this.multipic = multipic;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic2.zhimg.com/v2-b146b4bcddd4a51ac53b356e0de5dd6d.jpg
         * type : 0
         * id : 9572874
         * ga_prefix : 081608
         * title : 现在回头看，才更加感叹「中国队长」蔡赟是怎样的传奇
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
