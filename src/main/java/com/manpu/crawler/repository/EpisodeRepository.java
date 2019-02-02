package com.manpu.crawler.repository;

import com.manpu.crawler.entity.Episode;
import com.manpu.crawler.entity.generate.QEpisode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class EpisodeRepository implements CrudRepository<Episode, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <S extends Episode> S save(S s) {
		entityManager.persist(s);
		entityManager.flush();
		return s;
	}

	@Override
	public <S extends Episode> Iterable<S> saveAll(Iterable<S> iterable) {
		return null;
	}

	@Override
	public Optional<Episode> findById(Long aLong) {
		return null;
	}

	@Override
	public boolean existsById(Long aLong) {
		return false;
	}

	@Override
	public Iterable<Episode> findAll() {
		return null;
	}

	@Override
	public Iterable<Episode> findAllById(Iterable<Long> iterable) {
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
	public void delete(Episode episode) {

	}

	@Override
	public void deleteAll(Iterable<? extends Episode> iterable) {

	}

	@Override
	public void deleteAll() {

	}

	public Episode findOne(Episode episodeData, int episodeNumber) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QEpisode episode = QEpisode.episode;
		BooleanExpression express = episode.webtoon.eq(episodeData.getWebtoon()).and(episode.num.eq(Long.valueOf(episodeNumber)));
		return query.select(episode).from(episode).where(express).fetchOne();
	}

	public Episode findOneByNum(long episodeId, int episodeNumber) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QEpisode episode = QEpisode.episode;
		BooleanExpression express = episode.id.eq(episodeId).and(episode.num.eq(Long.valueOf(episodeNumber)));
		return query.select(episode).from(episode).where(express).fetchOne();
	}

	public void update(long id, Episode episodeData) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QEpisode episode = QEpisode.episode;
		BooleanExpression express = episode.id.eq(id);
		query.update(episode)
				.set(episode.free, episodeData.getFree())
				.set(episode.updatedAt, LocalDateTime.now())
				.where(express).execute();
	}

	public void updatePayEpisode(String id, List<Long> payEpisodeNumbers) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QEpisode episode = QEpisode.episode;
		BooleanExpression express = episode.webtoon.id.eq(id)
				.and(episode.num.in(payEpisodeNumbers.toArray(new Long[payEpisodeNumbers.size()])));
		query.update(episode)
				.set(episode.free, false)
				.set(episode.updatedAt, LocalDateTime.now())
				.where(express).execute();
	}
}
