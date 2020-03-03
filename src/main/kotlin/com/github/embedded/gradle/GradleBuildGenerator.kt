package com.github.embedded.gradle

import com.squareup.kotlinpoet.FileSpec
import org.gradle.internal.impldep.org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.GradleRunner
import java.io.File

import java.nio.file.Files
import java.nio.file.Path

class GradleBuildGenerator(buildTempDir: String) {

    private var tempDir: Path
    private var settingsFile: Path
    private var buildFile: Path
    private var asciiDocDir: Path
    private var sourceDir: Path
    private var testSourceDir: Path

    private var args: List<String> = listOf()

    init {
        File(buildTempDir).mkdir()
        tempDir = Path.of(buildTempDir)
        settingsFile = tempDir.resolve("settings.gradle")
        buildFile = tempDir.resolve("build.gradle")
        asciiDocDir = tempDir.resolve("src" + File.separator + "docs" + File.separator + "asciidoc")
        sourceDir = tempDir.resolve("src" + File.separator + "main" + File.separator + "kotlin")
        testSourceDir = tempDir.resolve("src" + File.separator + "test" + File.separator + "kotlin")
    }

    fun settingsFile(content: () -> String) {
        Files.write(settingsFile, content().toByteArray())
    }

    fun buildFile(content: () -> String) {
        Files.write(buildFile, content().toByteArray())
    }

    fun addAsciiDocFile(fileName: String, content: () -> String) {
        if (!asciiDocDir.toFile().exists()) asciiDocDir.toFile().mkdirs()
        Files.write(asciiDocDir.resolve(fileName), content().toByteArray())
    }

    fun addSourceFile(file: () -> FileSpec) {
        if (!sourceDir.toFile().exists()) sourceDir.toFile().mkdirs()
        file().writeTo(sourceDir)
    }

    fun addTestSourceFile(file: () -> FileSpec) {
        if (!testSourceDir.toFile().exists()) testSourceDir.toFile().mkdirs()
        file().writeTo(testSourceDir)
    }

    fun args(vararg args: String) {
        this.args = args.asList()
    }

    fun recycle() {
        FileUtils.deleteDirectory(tempDir.toFile())
    }

    fun runner(): GradleRunner {
        return GradleRunner.create()
                .withProjectDir(tempDir.toFile())
                .withArguments(args)
    }
}

@DslMarker
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.TYPE)
annotation class GradleBuildDsl

fun generateBuild(buildTempDir: String, block: (@GradleBuildDsl GradleBuildGenerator).() -> Unit): GradleBuildGenerator {
    return GradleBuildGenerator(buildTempDir).apply(block)
}

fun generateBuild(buildTempDir: Path, block: (@GradleBuildDsl GradleBuildGenerator).() -> Unit): GradleBuildGenerator {
    return GradleBuildGenerator(buildTempDir.toString()).apply(block)
}