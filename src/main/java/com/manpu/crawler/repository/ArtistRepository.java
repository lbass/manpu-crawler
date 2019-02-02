package com.manpu.crawler.repository;

import com.manpu.crawler.entity.Artist;
import com.manpu.crawler.entity.generate.QArtist;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class ArtistRepository implements CrudRepository<Artist, String> {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public <S extends Artist> S save(S s) {
		entityManager.persist(s);
		entityManager.flush();
		return s;
	}

	@Override
	public <S extends Artist> Iterable<S> saveAll(Iterable<S> iterable) {
		return null;
	}

	@Override
	public Optional<Artist> findById(String id) {
		return null;
	}

	@Override
	public boolean existsById(String id) {
		return false;
	}

	@Override
	public Iterable<Artist> findAll() {
		return null;
	}

	@Override
	public Iterable<Artist> findAllById(Iterable<String> iterable) {
		return null;
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public void deleteById(String aLong) {

	}

	@Override
	public void delete(Artist artist) {

	}

	@Override
	public void deleteAll(Iterable<? extends Artist> iterable) {

	}

	@Override
	public void deleteAll() {

	}

	public Artist findOne(String id) {
		JPAQueryFactory query = new JPAQueryFactory(entityManager);
		QArtist artist = QArtist.artist;
		BooleanExpression express = artist.id.eq(id);
		return query.select(artist).from(artist).where(express).fetchOne();
	}
}
