package edu.usm.cos420.antenatal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.usm.cos420.antenatal.controller.TestControllerParsing;
import edu.usm.cos420.antenatal.controller.TestControllerData;
import edu.usm.cos420.antenatal.service.TestService;

@RunWith(Suite.class)
@SuiteClasses({ TestService.class, TestControllerParsing.class, TestControllerData.class  })
public class AllTests {

}