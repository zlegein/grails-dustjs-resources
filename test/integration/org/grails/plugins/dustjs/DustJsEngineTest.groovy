package org.grails.plugins.dustjs

import org.springframework.core.io.ClassPathResource
/**
 * Created with IntelliJ IDEA.
 * User: zach
 * Date: 5/16/12
 * Time: 10:10 AM
 * To change this template use File | Settings | File Templates.
 */
class DustJsEngineTest extends grails.test.GrailsUnitTestCase {

    def dustjsEngine

    void setUp(){
        dustjsEngine = new DustjsEngine()
    }

    void testCompile(){
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        String output = dustjsEngine.compile(input)
        assert output.contains('This is a test') : "Output $output"

    }
}

