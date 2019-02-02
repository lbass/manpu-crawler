package com.manpu.crawler.repository;

import com.manpu.crawler.entity.Artist;
import com.manpu.crawler.entity.Webtoon;
import com.manpu.crawler.entity.WebtoonArtist;
import com.manpu.crawler.entity.generate.QWebtoonArtist;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class WebtoonArtistRepository implements CrudRepository<WebtoonArtist, Long> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <S extends WebtoonArtist> S save(S s) {
		entityManager.persist(s);
		entityManager.flush();
		return s;
	}

	@Override
	public <S extends WebtoonArtist> Iterable<S> saveAll(Iterable<S> iterable) {
		return null;
	}

	@Override
	public Optional<WebtoonArtist> findById(Long aLong) {
		return Optional.empty();
	}

	@Override
	public boolean existsById(Long aLong) {
		return false;
	}

	@Override
	public Iterable<WebtoonArtist> findAll() {
		return null;
	}

	@Override
	public Iterable<WebtoonArtist> findAllById(Iterable<Long> iterable) {
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
	public void delete(WebtoonArtist webtoonArtist) {

	}

	@Override
	public void deleteAll(Iterable<? extends WebtoonArtist> iterable) {

	}

	@Override
	public void deleteAll() {

	}

    public WebtoonArtist findById(Artist artistId, Webtoon webtoonId) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QWebtoonArtist webtoonArtist = QWebtoonArtist.webtoonArtist;
		BooleanExpression express = webtoonArtist.artist.eq(artistId)
				.and(webtoonArtist.webtoon.eq(webtoonId));
		return query.select(webtoonArtist).from(webtoonArtist).where(express).fetchOne();
    }
}
