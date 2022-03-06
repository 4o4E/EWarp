import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "com.e404"
version = "1.0.2"

repositories {
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    // BentBox
    maven(url = "https://repo.codemc.org/repository/maven-public/")
    // Vault
    maven(url = "https://jitpack.io")
    // Multiverse-Core
    maven("https://repo.onarandombox.com/content/groups/public/")
    // PlaceholderAPI
    maven(url = "https://repo.extendedclip.com/content/repositories/placeholderapi/")
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.13.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:spigot:1.13.2-R0.1-SNAPSHOT")
    // BentBox
    compileOnly("world.bentobox:bentobox:1.19.0-SNAPSHOT")
    // Vault
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    // Multiverse-Core
    compileOnly("com.onarandombox.multiversecore:Multiverse-Core:4.3.2-SNAPSHOT")
    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.0")
    // Bstats
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.shadowJar {
    archiveFileName.set("${project.name}-${project.version}.jar")
    relocate("org.bstats", "com.e404.bstats")
    exclude("META-INF/*")
    doFirst {
        for (file in File("jar").listFiles() ?: arrayOf()) {
            println("正在删除`${file.name}`")
            file.delete()
        }
    }

    doLast {
        File("jar").mkdirs()
        for (file in File("build/libs").listFiles() ?: arrayOf()) {
            println("正在复制`${file.name}`")
            file.copyTo(File("jar/${file.name}"), true)
        }
    }
}

tasks {
    processResources {
        filesMatching("plugin.yml") {
            expand(project.properties)
        }
    }
}