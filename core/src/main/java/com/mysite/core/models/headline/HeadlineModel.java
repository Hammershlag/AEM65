package com.mysite.core.models.headline;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeadlineModel {

    @Self
    private Resource resource;

    @SlingObject
    ResourceResolver resourceResolver;

    @ValueMapValue(name = JcrConstants.JCR_TITLE)
    private String title;

    @ValueMapValue(name = JcrConstants.JCR_DESCRIPTION)
    private String description;

    @PostConstruct
    public void init() {
        if (!getValid()) {
            title = Optional.ofNullable(resourceResolver)
                    .map(rr -> rr.adaptTo(PageManager.class))
                    .map(pm -> pm.getContainingPage(resource))
                    .map(Page::getContentResource)
                    .map(r -> r.adaptTo(ValueMap.class))
                    .map(vm -> vm.get(JcrConstants.JCR_TITLE, String.class))
                    .orElse(StringUtils.EMPTY);
        }
    }

    public boolean getValid() {
        return StringUtils.isNotBlank(title);
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
