import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.loom)
    alias(libs.plugins.git.hooks)
    alias(libs.plugins.detekt)
    alias(libs.plugins.shadow)
    `maven-publish`
}

val props = properties

val modId: String by project
val modName: String by project
val modVersion: String by project
val mavenGroup: String by project
val targetJavaVersion = 21
val javaVersion = JavaVersion.VERSION_21

version = "$modVersion${getVersionMetadata()}"
group = mavenGroup

repositories {
    maven("https://maven.architectury.dev/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    detektPlugins(libs.detekt)
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn.mappings) { classifier("v2") })
    modImplementation(libs.bundles.fabric)
    modImplementation(libs.architectury)
    modImplementation(libs.bundles.konf)
    shadow(libs.bundles.konf)
}

base {
    archivesName.set(modName)
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    withSourcesJar()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        register("tpaplusplus") {
            sourceSet("main")
            sourceSet("client")
        }
    }
}

tasks {
    processResources {
        val minecraftVersion: String by project
        val loaderVersion: String by project
        val kotlinLoaderVersion: String by project

        inputs.property("version", project.version)
        inputs.property("minecraft_version", minecraftVersion)
        inputs.property("loader_version", loaderVersion)
        filteringCharset = "UTF-8"

        filesMatching("fabric.mod.json") {
            expand(
                "version" to project.version,
                "minecraft_version" to minecraftVersion,
                "loader_version" to loaderVersion,
                "kotlin_loader_version" to kotlinLoaderVersion
            )
        }
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(targetJavaVersion)
    }

    withType<KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.fromTarget(targetJavaVersion.toString()))
    }

    jar {
        from("LICENSE")
    }

    shadowJar {
        from("LICENSE")

        configurations = listOf(
            project.configurations.shadow.get()
        )
        archiveClassifier.set("dev-all")

        exclude("kotlin/**", "kotlinx/**", "javax/**")
        exclude("org/intellij/**", "org/jetbrains/annotations/**")
        exclude("org/slf4j/**")

        val relocatePath = "net.superricky.tpaplusplus.libs."
        relocate("com.moandjiezana.toml", relocatePath + "com.moandjiezana.toml")
    }

    remapJar {
        dependsOn(shadowJar)
        inputFile = shadowJar.get().archiveFile
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = modName
            from(components["java"])
        }
    }

    repositories {
    }
}

detekt {
    buildUponDefaultConfig = true
    autoCorrect = true
    config.setFrom(rootProject.files("detekt.yml"))
}

gitHooks {
    setHooks(
        mapOf("pre-commit" to "detekt")
    )
}

fun getVersionMetadata(): String {
    val buildId = System.getenv("GITHUB_RUN_NUMBER")
    val workflow = System.getenv("GITHUB_WORKFLOW")

    if (workflow == "Release") {
        return ""
    }

    // CI builds only
    if (buildId != null) {
        return "+build.$buildId"
    }

    // No tracking information could be found about the build
    return "+nightly"
}
