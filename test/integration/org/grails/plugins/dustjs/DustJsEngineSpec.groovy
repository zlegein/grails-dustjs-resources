package org.grails.plugins.dustjs

import org.springframework.core.io.ClassPathResource
import grails.plugin.spock.IntegrationSpec
import spock.lang.Unroll

class DustJsEngineSpec extends IntegrationSpec {

    @Unroll("testing compile")
    void "compile"() {
        given:
        File input = (new ClassPathResource('org/grails/plugins/dustjs/example.dust', getClass().classLoader)).file
        when:
        def dustjsEngine = new DustjsEngine()
        String output = dustjsEngine.compile(input, "test-template-name")
        then:
        output.contains("This is a test")
        output.contains("dust.register(\"test-template-name\",body_0);")
    }
}
