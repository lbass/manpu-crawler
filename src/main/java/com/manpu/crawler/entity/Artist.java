package com.manpu.crawler.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@ToString
@Entity
public class Artist extends AbstractEntity {
	@Id
	@Column(length = 50)
	private String id;
	@Column(length = 50, nullable = false)
	private String crawledArtistId;
	@Column(length = 50, nullable = false)
	private String artistName;
	@Column(length = 50, nullable = false)
	private String site;
}
