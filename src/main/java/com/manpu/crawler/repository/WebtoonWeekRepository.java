package com.manpu.crawler.repository;

import com.manpu.crawler.constant.MANPU_CRAWLER_CONSTANT.WEEK;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.WebtoonWeek;
import com.manpu.crawler.entity.generate.QWebtoonWeek;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
public class WebtoonWeekRepository implements CrudRepository<WebtoonWeek, Long> {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public <S extends WebtoonWeek> S save(S s) {
        entityManager.persist(s);
        entityManager.flush();
        return s;
    }

    @Override
    public <S extends WebtoonWeek> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<WebtoonWeek> findById(Long aLong) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long aLong) {
        return false;
    }

    @Override
    public Iterable<WebtoonWeek> findAll() {
        return null;
    }

    @Override
    public Iterable<WebtoonWeek> findAllById(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(WebtoonWeek webtoonWeek) {

    }

    @Override
    public void deleteAll(Iterable<? extends WebtoonWeek> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    public WebtoonWeek findOne(Webtoon webtoon, WEEK day) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoonWeek webtoonWeek = QWebtoonWeek.webtoonWeek;
        BooleanExpression express = webtoonWeek.webtoon.eq(webtoon)
                .and(webtoonWeek.day.eq(day));

        return query.select(webtoonWeek).from(webtoonWeek).where(express).fetchOne();
    }

    public void update(WebtoonWeek webtoonWeekInfo) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoonWeek webtoonWeek = QWebtoonWeek.webtoonWeek;
        BooleanExpression express = webtoonWeek.webtoon.eq(webtoonWeekInfo.getWebtoon()).
                and(webtoonWeek.day.eq(webtoonWeekInfo.getDay()));
        query.update(webtoonWeek)
                .set(webtoonWeek.ranking, webtoonWeekInfo.getRanking())
                .where(express).execute();
    }

    public List<WebtoonWeek> findWeeKInfoByWebtoon(Webtoon webtoon) {
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        QWebtoonWeek webtoonWeek = QWebtoonWeek.webtoonWeek;
        BooleanExpression express = webtoonWeek.webtoon.eq(webtoon);
        return query.select(webtoonWeek).from(webtoonWeek).where(express).fetch();
    }

}
