plugins {
    kotlin("multiplatform") version "2.1.20"
}

val javaHome: String = System.getenv("JAVA_HOME")
    ?: System.getProperty("java.home")

group = "com.sschr15"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }

    jvm()
    listOf(
        linuxX64(),
//        mingwX64(),
//        linuxArm64(),
    ).forEach { target ->
        target.compilations.getByName("main") {
            cinterops { 
                val jni by creating {
                    defFile("src/nativeMain/cinterop/jni.def")
                    compilerOpts("-I${javaHome}/include", "-I${javaHome}/include/${target.name.takeWhile { it.isLowerCase() }}")
                }
            }
        }

        target.binaries { 
            sharedLib { 
                baseName = "jnikt"
                linkerOpts("-L${javaHome}/lib/server", "-ljvm")
            }
        }
    }

    sourceSets {
        jvmTest {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
            }
        }
    }
}

tasks.withType<JavaCompile>().configureEach { 
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-h", "${project.projectDir}/src/jvmMain/cinterop"))
}
