package com.manpu.crawler.constant;

public class MANPU_CRAWLER_CONSTANT {

    public enum SITE {
        NAVER,
        DAUM,
        LEZHIN,
        KAKAO
    }

    public enum WEEK {
        mon,
        tue,
        wed,
        thu,
        fri,
        sat,
        sun,
        na
    }

    public enum LEZHIN_WEEK {
        recommend(0),
        pass(1),
        sun(2),
        mon(3),
        tue(4),
        wed(5),
        thu(6),
        fri(7),
        sat(8),
        ten(9);

        private int index;
        private LEZHIN_WEEK(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }

    public enum DAUM_SERVICE_TYPE {
        preview,
        free,
        pay
    }

    public static int MAX_WORK_QUEUE_SIZE = 1000;
    public static int REQUEST_TIMEOUT = 10000;
    public static String DATETIME_FORMAT = "yyyyMMddHHmmss";

    public static class NAVER_CONSTANT {
        public static final String SITE_NAME = "NAVER";
        public static final String COMIC_HOST = "https://comic.naver.com";
        public static final String COMIC_DETAIL = "https://comic.naver.com/webtoon/list.nhn?titleId=";
        public static final String MAIN_URL = "/webtoon/weekdayList.nhn";
    }

    public static class DAUM_CONSTANT {
        public static final String SITE_NAME = "DAUM";
        public static final String COMIC_HOST = "http://webtoon.daum.net";
        public static final String MAIN_URL = "/data/pc/webtoon/list_serialized/%s?timeStamp=%d";
        public static final String DETAIL_PATH = "/m/webtoon/view/";
        public static final String DETAIL_API_URL = "/data/pc/webtoon/view/%s?timeStamp=%d";
        public static final String VIEWER_PATH = "http://m.webtoon.daum.net/m/webtoon/viewer/";
    }

    public static class LEZHIN_CONSTANT {
        public static final String COMIC_HOST = "https://www.lezhin.com";
        public static final String SITE_NAME = "LEZHIN";

        public static class V1 {
            public static final String MAIN_URL = "/ko/scheduled";
            public static final String DETAIL_PATH = "/ko/comic/";
            public static final String THUMBNAIL_PATH = "https://cdn.lezhin.com/v2/comics/%d/images/wide??width=" +
                    LEZHIN_CONSTANT.V1.THUMBNAIL_WIDTH + "&updated=%d";
            public static final String THUMBNAIL_WIDTH = "300";
            public static final String EPISODE_THUMBNAIL_WIDTH = "https://cdn.lezhin.com/v2/comics/%d/episodes/%d" +
                    "/images/banner?updated=%d&width=" + LEZHIN_CONSTANT.V1.THUMBNAIL_WIDTH;
        }

        public static class V2 {
            public static final String MAIN_URL = "/api/v2/inventory_groups/home_scheduled_k?platform=web&store=web&_=%d";
            public static final String RANKING_URL = "/api/v2/comics?offset=0&country_code=kr&adult_kind=kid&filter=all&limit=200&store=web&_=%d";
            public static final String THUMBNAIL_HOST = "https://cdn.lezhin.com";
        }
    }


    public static class KAKAO_CONSTANT {
        public static final String SITE_NAME = "KAKAO";
        public static final String COMIC_HOST = "https://api2-page.kakao.com/";
        public static final String MAIN_URL = "/api/v6/store/section_container/list?agent=web&category=10&subcategory=1000&day=0";
        public static final String THUMBNAIL_PATH = "https://dn-img-page.kakao.com/download/resource?kid=%s&filename=th2";
        public static final String DETAIL_PATH = "https://page.kakao.com/home?seriesId=%d";
        public static final String VIEWER_PATH = "https://page.kakao.com/viewer?productId=%d";
        public static final String DETAIL_API_PATH = "https://api2-page.kakao.com/api/v5/store/singles";
    }

}
