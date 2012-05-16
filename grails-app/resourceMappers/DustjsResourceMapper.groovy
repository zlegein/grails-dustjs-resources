/**
 * @author Zach Legein
 */
import org.grails.plugins.resource.mapper.MapperPhase
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication

import org.grails.plugins.dustjs.DustjsEngine

class DustjsResourceMapper implements GrailsApplicationAware {

    GrailsApplication grailsApplication

    def phase = MapperPhase.GENERATION // need to run early so that we don't miss out on all the good stuff

    static defaultExcludes = ['**/*.css','**/*.png','**/*.gif','**/*.jpg','**/*.jpeg','**/*.gz','**/*.zip']

    def map(resource, config){
        File originalFile = resource.processedFile
        File target

        if (resource.sourceUrl && originalFile.name.toLowerCase().endsWith('.dust')) {
            DustjsEngine engine = new DustjsEngine()
            File input = grailsApplication.parentContext.getResource(resource.sourceUrl).file
            target = new File(originalFile.absolutePath.replaceAll(/(?i)\.dust/, '.js'))

            if (log.debugEnabled) {
                log.debug "Compiling DUST file [${originalFile}] into [${target}]"
            }
            try {
                String output = engine.compile(input)
                target.write(output)
                // Update mapping entry
                // We need to reference the new js file from now on
                resource.processedFile = target
                // Not sure if i really need these
                resource.sourceUrlExtension = 'js'
                resource.actualUrl = resource.originalUrl.replaceAll(/(?i)\.dust/, '.js')
                resource.contentType = 'text/javascript'
                resource.tagAttributes.rel = 'javascript'
            } catch (Exception e) {
                log.error("error compiling dust file: ${originalFile}", e)
                e.printStackTrace()
            }
        }
    }
}
