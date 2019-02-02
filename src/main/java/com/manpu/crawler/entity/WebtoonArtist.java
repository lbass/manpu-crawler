package com.manpu.crawler.entity;

import com.manpu.crawler.entity.WebtoonArtist.WebtoonArtistId;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
@ToString
@Entity
@IdClass(WebtoonArtistId.class)
public class WebtoonArtist implements Serializable {

	/*
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition = "INT(10) UNSIGNED")
	private long id;
	*/
	@Id
	@ManyToOne
	@JoinColumn(name="webtoon_id", columnDefinition = "VARCHAR(50)")
	private Webtoon webtoon;

	@Id
	@ManyToOne
	@JoinColumn(name="artist_id", columnDefinition = "VARCHAR(50)")
	private Artist artist;

	public static class WebtoonArtistId implements Serializable {
		private String webtoon;
		private String artist;

		public WebtoonArtistId() { }

		public WebtoonArtistId(String webtoon, String artist) {
			this.webtoon = webtoon;
			this.artist = artist;
		}
	}
}
