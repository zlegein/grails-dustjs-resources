/**
 * @author Zach Legein
 */

import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.grails.plugin.resource.mapper.MapperPhase

class DustjsResourceMapper implements GrailsApplicationAware {

    GrailsApplication grailsApplication

    def phase = MapperPhase.GENERATION // need to run early so that we don't miss out on all the good stuff

    static defaultExcludes = ['**/*.css','**/*.png','**/*.gif','**/*.jpg','**/*.jpeg','**/*.gz','**/*.zip']
    static String DUST_FILE_EXTENSION = '.dust'

    def map(resource, config){
        File originalFile = resource.processedFile
        File target

        if (resource.sourceUrl && originalFile.name.toLowerCase().endsWith(DUST_FILE_EXTENSION)) {
            DustjsEngineService engine = new DustjsEngineService()
            File input = getOriginalFileSystemFile(resource.sourceUrl);
            target = new File(generateCompiledFileFromOriginal(originalFile.absolutePath))

            if (log.debugEnabled) {
                log.debug "Compiling DUST file [${originalFile}] into [${target}]"
            }
            try {
                engine.compile input, target
                // Update mapping entry
                // We need to reference the new js file from now on
                resource.processedFile = target
                // Not sure if i really need these
                resource.sourceUrlExtension = 'js'
                resource.actualUrl = generateCompiledFileFromOriginal(resource.originalUrl)
                resource.contentType = 'text/javascript'
                resource.tagAttributes.rel = 'javascript'
            } catch (Exception e) {
                log.error("error compiling dust file: ${originalFile}", e)
                e.printStackTrace()
            }
        }
    }

    private String generateCompiledFileFromOriginal(String original) {
         original.replaceAll(/(?i)\.dust/, '.js')
    }

    private File getOriginalFileSystemFile(String sourcePath) {
        grailsApplication.parentContext.getResource(sourcePath).file
    }
}
