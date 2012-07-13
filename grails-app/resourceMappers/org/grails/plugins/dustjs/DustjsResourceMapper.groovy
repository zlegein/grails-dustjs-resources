package org.grails.plugins.dustjs
/**
 * @author Zach Legein
 */
import org.grails.plugin.resource.mapper.MapperPhase
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication

import org.grails.plugins.dustjs.DustjsEngine
import org.codehaus.groovy.grails.exceptions.GrailsConfigurationException
import org.grails.plugin.resource.ResourceMeta

class DustjsResourceMapper implements GrailsApplicationAware {

    GrailsApplication grailsApplication

    def phase = MapperPhase.GENERATION // need to run early so that we don't miss out on all the good stuff

    static defaultExcludes = ['**/*.css','**/*.png','**/*.gif','**/*.jpg','**/*.jpeg','**/*.gz','**/*.zip']

    def map(resource, config){
        File originalFile = resource.processedFile

        if (resource.sourceUrl && originalFile.name.toLowerCase().endsWith('.dust')) {
            DustjsEngine engine = new DustjsEngine()
            File input = grailsApplication.parentContext.getResource(resource.sourceUrl).file
            String templateName = calculateTemplateName(resource, config)
            File target = new File(originalFile.absolutePath.replaceAll(/(?i)\.dust/, '.js'))

            if (log.debugEnabled) {
                log.debug "Compiling DUST file [${originalFile}] into [${target}]"
            }

            try {

                String output = engine.compile(input, templateName)
                target.write(output)
                // Update mapping entry
                // We need to reference the new js file from now on
                resource.processedFile = target
                // Not sure if i really need these
                resource.sourceUrlExtension = 'js'
                resource.actualUrl = resource.originalUrl.replaceAll(/(?i)\.dust/, '.js')
                resource.contentType = 'text/javascript'
            } catch (Exception e) {
                log.error("error compiling dust file: ${originalFile}", e)
                e.printStackTrace()
            }
        }
    }

    String calculateTemplateName(ResourceMeta resource, config) {
        String pathSeparator = getString(config, 'templatesPathSeparator', '/')
        String root = getString(config, 'templatesRoot')

        String templateName = resource.sourceUrl
        if (root) {
            if (!root.startsWith('/')) {
                root = '/' + root
            }
            if (!root.endsWith('/')) {
                root += '/'
            }
            if (templateName.startsWith(root)) {
                templateName -= root
            }
        }
        templateName = templateName.replaceAll(/(?i)\.dust/, '')
        templateName.split('/').findAll().join(pathSeparator)
    }

    private String getString(Map config, String key, String defaultVal = null) {
        config[key] instanceof String ? config[key] : defaultVal
    }



}
