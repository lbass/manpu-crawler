package com.manpu.crawler.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@Entity
public class Episode extends AbstractEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(columnDefinition = "INT(10) UNSIGNED")
	private long id;

	@ManyToOne
	@JoinColumn(name = "webtoon_id")
	private Webtoon webtoon;

	@Column(length = 4, nullable = false, columnDefinition = "INT(10) UNSIGNED")
	private long num;

	@Column(length = 300, nullable = false)
	private String url;

	@Column(length = 10)
	private String rating;

	@Column(length = 200)
	private String title;

	@Column(length = 300)
	private String thumbnailUrl;

	@Column
	private LocalDateTime uploadAt;

	@Column(nullable = false)
	private Boolean free = true;

	@Override
	public String toString() {
		return "Episode{" +
				"id=" + id +
				", webtoon=" + webtoon.getId() +
				", num=" + num +
				", url='" + url + '\'' +
				", rating='" + rating + '\'' +
				", title='" + title + '\'' +
				", thumbnailUrl='" + thumbnailUrl + '\'' +
				", uploadAt=" + uploadAt +
				", free=" + free +
				'}';
	}
}
