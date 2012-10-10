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

            def dustJsResource = (new ClassPathResource('org/grails/plugins/dustjs/dust-full-1.1.1.js', getClass().classLoader))
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

    def compile(File input, String templateName) {
        try {
            def cx = Context.enter()
            def compileScope = cx.newObject(globalScope)
            compileScope.setParentScope(globalScope)
            compileScope.put("dustJsSrc", compileScope, input.text)
            compileScope.put("dustJsSrcName", compileScope, templateName)
            log.debug "Compiling dust file under name: ${templateName}"
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
}