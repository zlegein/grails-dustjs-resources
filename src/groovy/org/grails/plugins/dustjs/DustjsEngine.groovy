package org.grails.plugins.dustjs

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.springframework.core.io.ClassPathResource

class DustjsEngine {

    def Scriptable globalScope
    def ClassLoader classLoader

    def DustjsEngine(){
        try {
            classLoader = getClass().getClassLoader()

            def dustJsResource = (new ClassPathResource('org/grails/plugins/dustjs/dust-full-0.4.0-modified.js', getClass().classLoader))
            assert dustJsResource.exists() : "DustJs resource not found"

            def dustJsStream = dustJsResource.inputStream

            Context cx = Context.enter()
            cx.setOptimizationLevel(-1)
            globalScope = cx.initStandardObjects()
            cx.evaluateReader(globalScope, new InputStreamReader(dustJsStream, 'UTF-8'), dustJsResource.filename, 0, null)
        } catch (Exception e) {
            throw new Exception("DustJs Engine initialization failed.", e)
        } finally {
            try {
                Context.exit()
            } catch (java.lang.IllegalStateException ise) {
                log.error ise
            }
        }
    }

    def compile(File input, String rootDir) {
        try {
            String filename = getFileName(input, rootDir)
            def cx = Context.enter()
            def compileScope = cx.newObject(globalScope)
            compileScope.setParentScope(globalScope)
            compileScope.put("dustJsSrc", compileScope, input.text)
            compileScope.put("dustJsSrcName", compileScope, filename)
            log.debug "Compiling dust file under name: ${filename}"
            def result = cx.evaluateString(compileScope, "dust.compile(dustJsSrc, dustJsSrcName)", "DustJs compile command", 0, null)
            return result
        } catch (Exception e) {
            throw new Exception("DustJs Engine compilation of dustjs to javascript failed.", e)
        } finally {
            try {
                Context.exit()
            } catch (java.lang.IllegalStateException ise) {
                log.error ise
            }
        }
    }

    def getFileName(File input, String rootDir) {
        List<String> folders = input.path.split('/')
        if(!folders.contains(rootDir)) {
            throw new IllegalArgumentException("Unable to locate root directory ${rootDir} in file path ${input.absolutePath}")
        }
        def start = folders.findIndexOf {it == rootDir} + 1
        def remainder = folders[start..folders.size() - 1].findAll{ it != input.name}
        if(!remainder.empty) {
            return "${remainder.join('_')}_${input.name.replaceAll(/.dust/, '')}"
        }  else {
            return input.name.replaceAll(/.dust/, '')
        }
    }
}