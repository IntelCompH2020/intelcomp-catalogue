package eu.intelcomp.catalogue.service;

import eu.intelcomp.catalogue.controller.WorkflowDatasetController;
import eu.intelcomp.catalogue.domain.Dataset;
import eu.intelcomp.catalogue.domain.User;
import eu.openminted.registry.core.domain.FacetFilter;
import eu.openminted.registry.core.domain.Paging;
import gr.athenarc.catalogue.service.GenericItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

@Service
public class WorkflowDatasetService implements DatasetService {

    private static final Logger logger = LoggerFactory.getLogger(WorkflowDatasetController.class);
    private static final String RESOURCE_TYPE = "dataset";
    private final GenericItemService genericItemService;

    public WorkflowDatasetService(GenericItemService genericItemService) {
        this.genericItemService = genericItemService;
    }

    @Override
    public Dataset get(String id) {
        return this.genericItemService.get(RESOURCE_TYPE, id);
    }

    @Override
    public Paging<Dataset> browse(FacetFilter filter) {
        filter.setResourceType(RESOURCE_TYPE);
        return this.genericItemService.getResults(filter);
    }

    @Override
    public Dataset add(String id, Dataset dataset) {
        Date now = new Date();
        String userId = User.getId(SecurityContextHolder.getContext().getAuthentication());

        dataset.setId(id);
        dataset.setOwner(userId);

        dataset.setCreationDate(now);
        dataset.setCreatedBy(userId);

        dataset.setModificationDate(now);
        dataset.setModifiedBy(userId);

        return genericItemService.add(RESOURCE_TYPE, dataset);
    }

    @Override
    public Dataset update(String id, Dataset dataset) {
        Dataset existing = get(id);

        dataset.setCreatedBy(existing.getCreatedBy());
        dataset.setCreationDate(existing.getCreationDate());

        Date now = new Date();
        String userId = User.getId(SecurityContextHolder.getContext().getAuthentication());
        dataset.setModificationDate(now);
        dataset.setModifiedBy(userId);

        try {
            return genericItemService.update(RESOURCE_TYPE, id, dataset);
        } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException e) {
            logger.error("Could not update dataset : {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void delete(String id) {
        genericItemService.delete(RESOURCE_TYPE, id);
    }

}
