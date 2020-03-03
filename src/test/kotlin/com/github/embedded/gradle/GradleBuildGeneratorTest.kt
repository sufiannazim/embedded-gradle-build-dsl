package com.github.embedded.gradle

import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GradleBuildGeneratorTest {

    @Test
    fun `test gradle build dsl`() {
        val gradleBuild = generateBuild("test-build") {

            settingsFile {
                """
                rootProject.name = "hello-world"
            """.trimIndent()
            }

            buildFile {
                """
                tasks.register("helloWorld") {
                    doLast {
                        println("Hello world!")
                    }
                }
            """.trimIndent()
            }



            args("helloWorld")

        }

        val result = gradleBuild.runner().build()

        assertTrue(result.output.contains("Hello world!"))
        assertEquals(TaskOutcome.SUCCESS, result.task(":helloWorld")!!.outcome)

        gradleBuild.recycle()
    }
}