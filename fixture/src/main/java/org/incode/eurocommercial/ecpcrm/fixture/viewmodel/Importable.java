package org.incode.eurocommercial.ecpcrm.fixture.viewmodel;

import java.util.List;

public interface Importable {

    /**
     * @param previousRow, if any.
     * @return created objects (so that the fixture framework can make available to calling test or in the UI)
     */
    List<Object> importData(Object previousRow);

    /**
     * Defines the order in which all {@link Importable}s are loaded.
     *
     * <p>
     *     NB: this is to replace the ImportOrder that has a hard-coded list of classes (WIP).
     * </p>
     */
    List<Class> importAfter();
}
