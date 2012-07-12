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

    def phase = MapperPhase.GENERATION

    static defaultIncludes = ['**/*.dust']

    def map(resource, config){
        File originalFile = resource.processedFile
        File input = getOriginalFileSystemFile(resource.sourceUrl)
        String templateName = calculateTemplateName(resource, config)

        if (resource.sourceUrl) {
            DustjsEngine engine = new DustjsEngine()
            File target = new File(originalFile.absolutePath.replaceAll(/(?i)\.dust/, '.js'))

            if (log.debugEnabled) {
                log.debug "Compiling DUST file [${originalFile}] into [${target}]"
            }

            try {
                engine.compile(input, target, templateName)
                // We need to reference the new js file from now on
                resource.processedFile = target
                resource.sourceUrlExtension = 'js'
                resource.contentType = 'text/javascript'
                resource.updateActualUrlFromProcessedFile()
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

    private File getOriginalFileSystemFile(String sourcePath) {
        grailsApplication.parentContext.getResource(sourcePath).file
    }


}
