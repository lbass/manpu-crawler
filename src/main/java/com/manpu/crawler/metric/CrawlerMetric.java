package com.manpu.crawler.metric;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class CrawlerMetric {
    private AtomicLong naverEpisodeWorkCount = new AtomicLong();
    private AtomicLong daumEpisodeWorkCount = new AtomicLong();
    private AtomicLong lezhinEpisodeWorkCount = new AtomicLong();
    private AtomicLong kakaoEpisodeWorkCount = new AtomicLong();

    private AtomicLong naverListWorkCount = new AtomicLong();
    private AtomicLong daumListWorkCount = new AtomicLong();
    private AtomicLong lezhinListWorkCount = new AtomicLong();
    private AtomicLong kakaoListWorkCount = new AtomicLong();

    private AtomicLong naverListWorkErrorCount = new AtomicLong();
    private AtomicLong daumListWorkErrorCount = new AtomicLong();
    private AtomicLong lezhinListWorkErrorCount = new AtomicLong();
    private AtomicLong kakaoListWorkErrorCount = new AtomicLong();

    private AtomicLong processCount = new AtomicLong();
    private AtomicLong episodeInsertCount = new AtomicLong();
    private AtomicLong listErrorWorkCount = new AtomicLong();
    private AtomicLong episodeErrorWorkCount = new AtomicLong();

    private AtomicLong pushApiCount = new AtomicLong();
    private AtomicLong pushApiTotalLatency = new AtomicLong();

    public long getNaverEpisodeWorkCount() {
        return naverEpisodeWorkCount.get();
    }

    public void addNaverEpisodeWorkCount() {
        naverEpisodeWorkCount.incrementAndGet();
    }

    public long getDaumEpisodeWorkCount() {
        return daumEpisodeWorkCount.get();
    }

    public void addDaumEpisodeWorkCount() {
        daumEpisodeWorkCount.incrementAndGet();
    }

    public long getLezhinEpisodeWorkCount() {
        return lezhinEpisodeWorkCount.get();
    }

    public void addLezhinEpisodeWorkCount() {
        lezhinEpisodeWorkCount.incrementAndGet();
    }

    public long getKakaoEpisodeWorkCount() {
        return kakaoEpisodeWorkCount.get();
    }

    public void addKakaoEpisodeWorkCount() {
        kakaoEpisodeWorkCount.incrementAndGet();
    }

    public long getNaverListWorkCount() {
        return naverListWorkCount.get();
    }

    public void addNaverListWorkCount() {
        naverListWorkCount.incrementAndGet();
    }

    public long getDaumListWorkCount() {
        return daumListWorkCount.get();
    }

    public void addDaumListWorkCount() {
        daumListWorkCount.incrementAndGet();
    }

    public long getLezhinListWorkCount() {
        return lezhinListWorkCount.get();
    }

    public void addLezhinListWorkCount() {
        lezhinListWorkCount.incrementAndGet();
    }

    public long getKakaoListWorkCount() {
        return kakaoListWorkCount.get();
    }

    public void addKakaoListWorkCount() {
        kakaoListWorkCount.incrementAndGet();
    }

    public long getProcessCount() {
        return processCount.get();
    }

    public void addProcessCount() {
        processCount.incrementAndGet();
    }

    public AtomicLong getNaverListWorkErrorCount() {
        return naverListWorkErrorCount;
    }

    public void addNaverListWorkErrorCount() {
        naverListWorkErrorCount.incrementAndGet();
    }

    public AtomicLong getDaumListWorkErrorCount() {
        return daumListWorkErrorCount;
    }

    public void addDaumListWorkErrorCount() {
        daumListWorkErrorCount.incrementAndGet();
    }

    public AtomicLong getLezhinListWorkErrorCount() {
        return lezhinListWorkErrorCount;
    }

    public void addLezhinListWorkErrorCount() {
        lezhinListWorkErrorCount.incrementAndGet();
    }

    public AtomicLong getKakaoListWorkErrorCount() {
        return kakaoListWorkErrorCount;
    }

    public void addKakaoListWorkErrorCount() {
        kakaoListWorkErrorCount.incrementAndGet();
    }

    public AtomicLong getEpisodeErrorWorkCount() {
        return episodeErrorWorkCount;
    }

    public void addEpisodeErrorWorkCount() {
        episodeErrorWorkCount.incrementAndGet();
    }

    public AtomicLong getEpisodeInsertCount() {
        return episodeInsertCount;
    }

    public void addEpisodeInsertCount() {
        episodeInsertCount.incrementAndGet();
    }

    public void addPushApiCount() {
        pushApiCount.incrementAndGet();
    }

    public void addPushApiTotalLatency(long takeTime) {
        pushApiTotalLatency.addAndGet(takeTime);
    }

    public long getPushApiAverage() {
        long count = pushApiCount.get();
        if(count > 0) {
            return pushApiTotalLatency.get() / count;
        }
        return 0;
    }

    public void clearMetric() {
        naverEpisodeWorkCount.set(0L);
        daumEpisodeWorkCount.set(0L);
        lezhinEpisodeWorkCount.set(0L);
        kakaoEpisodeWorkCount.set(0L);

        naverListWorkCount.set(0L);
        daumListWorkCount.set(0L);
        lezhinListWorkCount.set(0L);
        kakaoListWorkCount.set(0L);

        naverListWorkErrorCount.set(0L);
        daumListWorkErrorCount.set(0L);
        lezhinListWorkErrorCount.set(0L);
        kakaoListWorkErrorCount.set(0L);

        episodeInsertCount.set(0L);
        processCount.set(0L);
        listErrorWorkCount.set(0L);
        episodeErrorWorkCount.set(0L);

        if(pushApiCount.get() > 20) {
            pushApiCount.set(0L);
            pushApiTotalLatency.set(0L);
        }
    }

}
