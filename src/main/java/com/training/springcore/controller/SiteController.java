package com.training.springcore.controller;

import com.training.springcore.config.SecurityConfig;
import com.training.springcore.exception.NotFoundException;
import com.training.springcore.model.Site;
import com.training.springcore.repository.CaptorDao;
import com.training.springcore.repository.MeasureDao;
import com.training.springcore.repository.SiteDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/sites")
@Transactional
public class SiteController {
    @Autowired
    SiteDao siteDao;
    @Autowired
    MeasureDao measureDao;
    @Autowired
    CaptorDao captorDao;

    @GetMapping
    public ModelAndView list() {
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

    @GetMapping("/{id}")
    public ModelAndView findById(@PathVariable String id) {
        return new ModelAndView("site_create")
                .addObject("site",
                        siteDao.findById(id).orElseThrow(NotFoundException::new));
    }

    @Secured(SecurityConfig.ROLE_ADMIN)
    @GetMapping("/create")
    public ModelAndView create(Model model) {
        return new ModelAndView("site_create").addObject("site", new Site());
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ModelAndView save(Site site) {
        if (site.getId() == null) {
            return new ModelAndView("site_create").addObject("site", siteDao.save(site));
        } else {
            Site siteToPersist =
                    siteDao.findById(site.getId()).orElseThrow(NotFoundException::new);

            // L'utilisateur ne peut changer que le nom du site sur l'écran
            siteToPersist.setName(site.getName());
            return new ModelAndView("sites").addObject("sites", siteDao.findAll());
        }
    }

    @Secured(SecurityConfig.ROLE_ADMIN)
    @PostMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable String id) {
        // Comme les capteurs sont liés à un site et les mesures sont liées à un capteur, nous devons faire
        // le ménage avant pour ne pas avoir d'erreur à la suppression d'un site utilisé ailleurs dans la base
        Site site = siteDao.findById(id).orElseThrow(NotFoundException::new);
        site.getCaptors().forEach(c -> measureDao.deleteByCaptorId(c.getId()));
        captorDao.deleteBySiteId(id);
        siteDao.deleteById(id);
        return new ModelAndView("sites").addObject("sites", siteDao.findAll());
    }

    @GetMapping("/{id}/measures")
    public ModelAndView findMeasuresById(@PathVariable String id) {
        Site site = siteDao.findById(id).orElseThrow(NotFoundException::new);
    // Comme les templates ont une intelligence limitée on concatène ici les id de captor dans une chaine
    // de caractères qui pourra être exeploitée tel quelle
        String captors = site.getCaptors()
                .stream()
                .map(c -> "{ id: '" + c.getId() + "', name: '" + c.getName()
                        + "'}")
                .collect(Collectors.joining(","));
        return new ModelAndView("site-measures")
                .addObject("site", site)
                .addObject("captors", captors);
    }
}
