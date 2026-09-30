package br.com.fabioperettig;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ClienteDAOTest.class,
        ProdutoDAOTest.class
})
public class AllTests { }
