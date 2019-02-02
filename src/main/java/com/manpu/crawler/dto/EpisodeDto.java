package com.manpu.crawler.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class EpisodeDto {
	private String url;
	private int episodeNumber;
	private String rating;
	private String updateDate;
	private String episodeTitle;
	private String episodeThumbnailUrl;
	private Boolean free = true;

}
