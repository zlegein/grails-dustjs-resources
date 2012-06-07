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
    def grailsApplication

    void setUp(){
        dustjsEngine = new DustjsEngine()
    }

    void testCompile(){
        File input = grailsApplication.parentContext.getResource('/WEB-INF/dust/test.dust').file
        String output = dustjsEngine.compile(input)
        assert output.contains('This is a test') : "Output $output"

    }
    void testFileName(){
        File input = grailsApplication.parentContext.getResource('/WEB-INF/dust/test.dust').file
        //File input = (new ClassPathResource('test.dust', getClass().classLoader)).file
        assertEquals("test", dustjsEngine.getFileName(input, 'dust'))
        assertEquals("org_grails_plugins_dustjs_example", dustjsEngine.getFileName(input, ''))

    }

}

