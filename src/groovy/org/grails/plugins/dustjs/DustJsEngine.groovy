package org.grails.plugins.dustjs

import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.springframework.core.io.ClassPathResource

class DustJsEngine {

    def Scriptable globalScope
    def ClassLoader classLoader

    def DustJsEngine(){
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
            } catch (java.lang.IllegalStateException ise) {}
        }
    }

    def compile(File input) {
        try {
            def cx = Context.enter()
            def compileScope = cx.newObject(globalScope)
            compileScope.setParentScope(globalScope)
            compileScope.put("dustJsSrc", compileScope, input.text)
            def filename = "${input.parentFile.name}_${input.name.replaceAll(/.dust/, '')}"
            compileScope.put("dustJsSrcName", compileScope, filename)
            log.debug "Compiling dust file under name: ${filename}"
            def result = cx.evaluateString(compileScope, "dust.compile(dustJsSrc, dustJsSrcName)", "DustJs compile command", 0, null)
            return result
        } catch (Exception e) {
            throw new Exception("CoffeeScript Engine compilation of coffeescript to javascript failed.", e)
        } finally {
            Context.exit()
        }
    }
}