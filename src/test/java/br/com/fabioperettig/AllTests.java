package br.com.fabioperettig;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ClienteDAOTest.class})
public class AllTests { }
