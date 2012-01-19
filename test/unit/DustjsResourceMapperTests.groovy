import org.gmock.WithGMock
import org.junit.Test
import grails.test.GrailsUnitTestCase
import org.grails.plugin.resource.mapper.MapperPhase
import org.gmock.GMockTestCase

/**
 * @ Zach Legein
 */


class DustjsResourceMapperTests extends GMockTestCase {

    DustjsResourceMapper mapper

    void setUp() {
        mapper = new DustjsResourceMapper()
        mapper.metaClass.log = [debug: {}, error: {}]
    }


    void testMapperGeneratesJsFromDustResource() {
        String fileName = "test.dust"
        def targetFile = mock(File, constructor('/var/file/test.js'))

        def originalFile = mock(File)
        def processedFile = mock(File)
        processedFile.getName().returns(fileName).stub()
        processedFile.getAbsolutePath().returns('/var/file/' + fileName).stub()

        def dustjsEngine = mock(DustjsEngineService, constructor())
        dustjsEngine.compile(originalFile, targetFile).once()

        def resource = [processedFile: processedFile, sourceUrl: fileName, actualUrl: '', sourceUrlExtension: 'dust', contentType: '', originalUrl: 'file.dust', tagAttributes: [rel: 'javascript/dust']]
        def config = [:]

        def mockedMapper = mock(mapper)
        mockedMapper.getOriginalFileSystemFile(fileName).returns(originalFile)

        play {
            mockedMapper.map(resource, config)
            assertEquals 'file.js', resource.actualUrl
            assertEquals 'js', resource.sourceUrlExtension
            assertEquals 'javascript', resource.tagAttributes.rel
            assertEquals 'text/javascript', resource.contentType
        }
    }

    void testMapperHandlesUpperCaseFileExtension() {
        String fileName = "test.DUST"
        def targetFile = mock(File, constructor('/var/file/test.js'))

        def originalFile = mock(File)
        def processedFile = mock(File)
        processedFile.getName().returns(fileName).stub()
        processedFile.getAbsolutePath().returns('/var/file/' + fileName).stub()

        def dustjsEngine = mock(DustjsEngineService, constructor())
        dustjsEngine.compile(originalFile, targetFile).once()

        def mockedMapper = mock(mapper)
        mockedMapper.getOriginalFileSystemFile(fileName).returns(originalFile)

        def resource = [processedFile: processedFile, sourceUrl: fileName, actualUrl: '', sourceUrlExtension: 'DUST', contentType: '', originalUrl: 'test.DUST', tagAttributes: [rel: 'javascript/dust']]
        def config = [:]
        play {
            mockedMapper.map(resource, config)
            assertEquals 'test.js', resource.actualUrl
            assertEquals 'js', resource.sourceUrlExtension
            assertEquals 'javascript', resource.tagAttributes.rel
            assertEquals 'text/javascript', resource.contentType
        }
    }


    void testMapperRunsEarlyInProcessingPipeline() {
        assertEquals MapperPhase.GENERATION, mapper.phase
    }

    void testMapperExcludesAllButCSS() {
        assertTrue(mapper.defaultExcludes.contains('**/*.css'))
        assertTrue(mapper.defaultExcludes.contains('**/*.png'))
        assertTrue(mapper.defaultExcludes.contains('**/*.gif'))
        assertTrue(mapper.defaultExcludes.contains('**/*.jpg'))
        assertTrue(mapper.defaultExcludes.contains('**/*.jpeg'))
        assertTrue(mapper.defaultExcludes.contains('**/*.gz'))
        assertTrue(mapper.defaultExcludes.contains('**/*.zip'))
    }

    void testMapperOnlyProcessesDustFiles() {
        def processedFile = mock(File)
        processedFile.getName().returns('notdust.js')
        def resource = [processedFile: processedFile, sourceUrl: 'notdust.js', actualUrl: 'notdust.js', sourceUrlExtension: 'js', contentType: '', originalUrl: 'notdust.js', tagAttributes: [rel: 'javascript']]
        def config = [:]
        play {
            mapper.map(resource, config)
            assertEquals 'notdust.js', resource.actualUrl
            assertEquals 'js', resource.sourceUrlExtension
            assertEquals 'javascript', resource.tagAttributes.rel
            assertEquals '', resource.contentType
        }

    }

    void testGeneratedFilename() {
        assertEquals 'foo/bar.js', mapper.generateCompiledFileFromOriginal('foo/bar.dust')
        assertEquals 'foo/bar.js', mapper.generateCompiledFileFromOriginal('foo/bar.DUST')
        assertEquals 'foo/./bar.js', mapper.generateCompiledFileFromOriginal('foo/./bar.dust')
        assertEquals 'foo/dust/bar.js', mapper.generateCompiledFileFromOriginal('foo/dust/bar.dust')
    }

}
