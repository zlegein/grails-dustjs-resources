package org.grails.plugins.dustjs

import org.springframework.core.io.ClassPathResource
import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

class DustJsEngineSpec extends IntegrationSpec {
    def grailsApplication

    @Unroll("testing complie")
    void "compile"() {
        given:
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        when:
        def dustjsEngine = new DustjsEngine()
        String output = dustjsEngine.compile(input, "dustjs")
        then:
        assert output.contains("This is a test")
    }

    @Unroll("testing compile failure")
    void "compile failure"() {
        given:
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        when:
        def dustjsEngine = new DustjsEngine()
        String output = dustjsEngine.compile(input, "notadir")
        then:
        thrown Exception
    }

    @Unroll("testing getFileName #path")
    void "getFileName"() {
        given:
        File input = grailsApplication.parentContext.getResource("/WEB-INF/${path}").file
        when:
        def dustjsEngine = new DustjsEngine()
        then:
        assert filename == dustjsEngine.getFileName(input, grailsApplication.config.dustjs.srcRootDir)
        where:
        path                        | filename
        "dust/test.dust"            | "test"
        "dust/dir1/test.dust"       | "dir1_test"
        "dust/dir1/dir2/test.dust"  | "dir1_dir2_test"
    }
}

