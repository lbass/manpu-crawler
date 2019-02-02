package com.manpu.crawler.pagecrawler;

import com.google.common.collect.ForwardingSet;
import com.manpu.crawler.entity.Webtoon;
import com.querydsl.codegen.GenericExporter;
import com.querydsl.codegen.Keywords;
import com.querydsl.codegen.PropertyHandling;
import org.junit.Test;

import javax.persistence.*;
import java.io.File;

public class QuerydslCodeGenerator {

    @Test
    public void generateCode() {
        GenericExporter exporter = new GenericExporter();
        exporter.setKeywords(Keywords.JPA);
        exporter.setEntityAnnotation(Entity.class);
        exporter.setEmbeddableAnnotation(Embeddable.class);
        exporter.setEmbeddedAnnotation(Embedded.class);
        exporter.setSupertypeAnnotation(MappedSuperclass.class);
        exporter.setSkipAnnotation(Transient.class);
        exporter.setTargetFolder(new File("src/main/java"));
        exporter.setPackageSuffix(".generate");
        exporter.setPropertyHandling(PropertyHandling.JPA);
        exporter.addStopClass(ForwardingSet.class);
        exporter.export(Webtoon.class.getPackage());
    }
}
