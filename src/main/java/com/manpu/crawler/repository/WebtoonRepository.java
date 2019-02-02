package com.manpu.crawler.repository;


import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.SITE;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.generate.QWebtoon;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WebtoonRepository implements CrudRepository<Webtoon, String> {

    private Logger logger = LoggerFactory.getLogger(WebtoonRepository.class);

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public <S extends Webtoon> S save(S s) {
        entityManager.persist(s);
        entityManager.flush();
        return s;
    }

    @Override
    public <S extends Webtoon> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Webtoon> findById(String id) {
        return null;
    }

    public Webtoon findOne(String id) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoon webtoon = QWebtoon.webtoon;
        BooleanExpression express = webtoon.id.eq(id);
        return query.select(webtoon).from(webtoon).where(express).fetchOne();
    }


    @Override
    public boolean existsById(String id) {
        return false;
    }

    @Override
    public Iterable<Webtoon> findAll() {
        return null;
    }

    @Override
    public Iterable<Webtoon> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public void delete(Webtoon webtoon) {

    }

    @Override
    public void deleteAll(Iterable<? extends Webtoon> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    public List<Webtoon> findInSerial() {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoon webtoon = QWebtoon.webtoon;
        BooleanExpression express = webtoon.serialState.eq(Webtoon.SerialState.ACTIVE);
        return query.select(webtoon).from(webtoon).where(express).fetch();
    }

    public void updateLastEpisode(Webtoon webtoonInfo) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoon webtoon = QWebtoon.webtoon;
        BooleanExpression express = webtoon.id.eq(webtoonInfo.getId());
        query.update(webtoon)
                .set(webtoon.lastEpisodeNum, webtoonInfo.getLastEpisodeNum())
                .set(webtoon.webtoonMainUrl, webtoonInfo.getWebtoonMainUrl())
                .set(webtoon.lastUpdateAt, webtoonInfo.getLastUpdateAt())
                .set(webtoon.updatedAt, LocalDateTime.now())
                .where(express).execute();
    }

    public void updateBatchRakingInfo(List<Webtoon> webtoonList) {
        QWebtoon qWebtoon = QWebtoon.webtoon;
        for(Webtoon webtoon : webtoonList) {
            JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, QWebtoon.webtoon);
            updateClause
                    .set(qWebtoon.rating, webtoon.getRating())
                    .where(qWebtoon.id.eq(webtoon.getId()))
                    .execute();
        }
    }

    public List<Webtoon> findNaverAndDaumWebtoon() {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoon webtoon = QWebtoon.webtoon;
        BooleanExpression express = webtoon
                .serialState
                .eq(Webtoon.SerialState.ACTIVE)
                .and(
                        webtoon.site.eq(SITE.NAVER.name())
                ).or(
                        webtoon.site.eq(SITE.DAUM.name())
                );

        return query.select(webtoon).from(webtoon).where(express).fetch();
    }

    public List<Webtoon> findBySite(SITE naver) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoon webtoon = QWebtoon.webtoon;
        BooleanExpression express = webtoon.site.eq(naver.name());
        return query.select(webtoon).from(webtoon).where(express).fetch();

    }
}
