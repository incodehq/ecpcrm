package org.incode.eurocommercial.ecpcrm.fixture.jdbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.google.common.collect.Lists;

import org.datanucleus.enhancement.Persistable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.bookmark.BookmarkService;
import org.apache.isis.applib.services.registry.ServiceRegistry2;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.incode.eurocommercial.ecpcrm.fixture.viewmodel.Importable;

public class JdbcFixture extends FixtureScript {

    private final List<Class> classes;

    //private final Map<Class, List<Object>> objectsByClass;
    private final List objects;

    private String userName;
    private String password;
    private String database;

    private static final Logger LOG = LoggerFactory.getLogger(JdbcFixture.class);

    public JdbcFixture(String database, String username, String password, Class cls) {
        this(database, username, password, Arrays.asList(cls));
    }


    public JdbcFixture(String database, String username, String password, Class... classes) {
        this(database, username, password, Arrays.asList(classes));
    }

    public JdbcFixture(String database, String username, String password, List<Class> classes) {
        this.database = database;
        this.userName = username;
        this.password = password;

        // this.objectsByClass = Maps.newHashMap();
        this.objects = Lists.newArrayList();
        this.classes = classes;

        for (Class cls : classes) {
            if (!Importable.class.isAssignableFrom(cls) && !Persistable.class.isAssignableFrom(cls)) {
                throw new IllegalArgumentException(String.format(
                        "Class '%s' does not implement '%s', nor is it persistable",
                        cls.getSimpleName(), Importable.class.getSimpleName() ));
            }
        };
    }

    protected void execute(FixtureScript.ExecutionContext ec) {

        final JdbcClient client = JdbcClient.withDatabase(database, userName, password);

        for (Class cls : this.classes) {
            LOG.info("Start import {}", cls);

            List rowObjects = client.fromResultSet(cls);
            Object previousRow = null;

            Object rowObj;
            for (Iterator iterator = rowObjects.iterator(); iterator.hasNext(); previousRow = rowObj) {
                rowObj = iterator.next();
                this.create(rowObj, ec, previousRow);
                transactionService.nextTransaction();
            }
        }
        LOG.info("Finished import");

    }

    private List<Object> create(Object rowObj, FixtureScript.ExecutionContext ec, Object previousRow) {
        if (rowObj instanceof Importable) {
            Importable importable = (Importable) rowObj;
            serviceRegistry2.injectServicesInto(importable);
            return importable.importData(previousRow);
        } else {
            this.repositoryService.persist(rowObj);
            ec.addResult(this, rowObj);
            return Collections.singletonList(rowObj);
        }
    }

//    private void addToMap(Class cls, List<Object> createdObjects) {
//        Object objectList = (List) this.objectsByClass.get(cls);
//        if (objectList == null) {
//            objectList = Lists.newArrayList();
//            this.objectsByClass.put(cls, (List<Object>) objectList);
//        }
//
//        ((List) objectList).addAll(createdObjects);
//    }
//
//    private void addToCombined(List<Object> createdObjects) {
//        this.objects.addAll(createdObjects);
//    }

    public boolean equals(Object o) {
        return hashCode() == o.hashCode();
    }

    public int hashCode() {
        return classes.hashCode();
    }

//    public Map<Class, List<Object>> getObjectsByClass() {
//        return this.objectsByClass;
//    }

    public List getObjects() {
        return this.objects;
    }

    @Inject
    private TransactionService transactionService;

    @Inject
    private ServiceRegistry2 serviceRegistry2;

    @Inject
    private RepositoryService repositoryService;

    @Inject
    private BookmarkService bookmarkService;

}
