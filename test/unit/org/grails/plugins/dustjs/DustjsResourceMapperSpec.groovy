package org.grails.plugins.dustjs

import grails.plugin.spock.UnitSpec
import spock.lang.Unroll
import org.grails.plugin.resource.ResourceMeta

/**
 * Created with IntelliJ IDEA.
 * User: zach
 * Date: 7/12/12
 * Time: 6:28 AM
 * To change this template use File | Settings | File Templates.
 */
class DustjsResourceMapperSpec extends UnitSpec {

    DustjsResourceMapper mapper

    def setup() {
        mapper = new DustjsResourceMapper()
    }

    @Unroll()
    def "test calculateTemplateName "() {
        given:
        ResourceMeta resource = new ResourceMeta()
        Map config = [:]
        when:
        resource.sourceUrl = source
        config.templatesRoot = root
        config.templatesPathSeparator = separator
        def name = mapper.calculateTemplateName(resource, config)
        then:
        name == result
        where:
        source                         |  result             | root            |  separator
        '/templates/test.dust'         | 'templates/test'    | null            | null
        '/js/test.dust'                | 'js/test'           | null            | null
        '/js/test.dust'                | 'test'              | 'js'            | null
        '/js/templates/test.dust'      | 'test'              | '/js/templates' | null
        '/js/templates/test.dust'      | 'test'              | 'js/templates/' | null
        '/js/templates/test.dust'      | 'test'              | '/js/templates' | null
        '/js/templates/test.dust'      | 'js/templates/test' | 'templates' | null
        '/templates/test.html'         | 'test.html'         | 'templates' | null
        '/templates/foo/bar/test.dust' | 'foo-bar-test'      | 'templates' | '-'
        '/templates/foo/bar/test.dust' | 'foo_bar_test'      | 'templates' | '_'
    }
}