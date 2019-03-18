package com.training.springcore.controller;

import com.training.springcore.model.FixedCaptor;
import com.training.springcore.model.Site;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.repository.MeasureDao;
import com.training.springcore.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/sites/{siteId}/captors/SIMULATED")
@Transactional
public class SimulatedCaptorController {
    @Autowired
    SiteDao siteDao;
    @Autowired
    MeasureDao measureDao;
    @Autowired
    CaptorDao captorDao;

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("captor").addObject("captor", captorDao.findAll());
    }

    @GetMapping("/create")
    public ModelAndView create(@PathVariable String siteId, Model model) {
        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);
        return new ModelAndView("captor")
                .addObject("site", site)
                .addObject("captor", new FixedCaptor());
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(@PathVariable String siteId, FixedCaptor captor) {

        Site site = siteDao.findById(siteId).orElseThrow(IllegalArgumentException::new);
        FixedCaptor captorToPersist;

        if (captor.getId() == null) {
            captorToPersist = new FixedCaptor(captor.getName(), site,
                    captor.getDefaultPowerInWatt());
        } else {
            captorToPersist = (FixedCaptor) captorDao.findById(captor.getId())
                    .orElseThrow(IllegalArgumentException::new);
            captorToPersist.setName(captor.getName());
            captorToPersist.setDefaultPowerInWatt(captor.getDefaultPowerInWatt());
        }
        captorDao.save(captorToPersist);
        return new ModelAndView("site_create").addObject("site", site);

    }


}
