package com.training.springcore.service;

import com.training.springcore.config.Monitored;
import com.training.springcore.model.Site;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;


@Service
public class SiteServiceImpl implements SiteService {

    private ResourceLoader resourceLoader;

    private CaptorService captorService;

    private final static Logger logger = LoggerFactory.getLogger(SiteService.class);

    public SiteServiceImpl(CaptorService captorService, ResourceLoader resourceLoader) {
        logger.debug("Init SiteServiceImpl :", this);
        this.captorService = captorService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    @Monitored
    public Site findById(String siteId) {
        logger.debug("Appel de findById :", this);
        if (siteId == null) {
            return null;
        }

        Site site = new Site("Florange");
        site.setId(siteId);
        site.setCaptors(captorService.findBySite(siteId));
        return site;
    }


}
