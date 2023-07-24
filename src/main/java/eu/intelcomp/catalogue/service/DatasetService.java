package eu.intelcomp.catalogue.service;

import eu.intelcomp.catalogue.domain.Dataset;
import eu.openminted.registry.core.domain.FacetFilter;
import eu.openminted.registry.core.domain.Paging;

public interface DatasetService {

    Dataset get(String id);

    Paging<Dataset> browse(FacetFilter filter);

    Dataset add(String id, Dataset dataset);

    Dataset update(String id, Dataset dataset);

    void delete(String id);

}
