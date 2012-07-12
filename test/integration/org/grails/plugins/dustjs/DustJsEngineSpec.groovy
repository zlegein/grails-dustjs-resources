package org.grails.plugins.dustjs

import org.springframework.core.io.ClassPathResource
import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

class DustJsEngineSpec extends IntegrationSpec {

    @Unroll("testing compile")
    void "compile"() {
        given:
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        File target = new File(input.absolutePath.replaceAll(/(?i)\.dust/, '.js'))
        when:
        !target.exists()
        def dustjsEngine = new DustjsEngine()
        dustjsEngine.compile(input, target, "doesn't matter for this test")
        then:
        target.text.contains("This is a test")
    }

    @Unroll("testing compile failure")
    void "compile failure"() {
        given:
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        when:
        def dustjsEngine = new DustjsEngine()
        dustjsEngine.compile(input, null, "whatever")
        then:
        thrown Exception
    }
}

