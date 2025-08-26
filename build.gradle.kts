@file:Suppress("PropertyName", "SpellCheckingInspection")

import io.izzel.taboolib.gradle.Basic
import io.izzel.taboolib.gradle.Bukkit
import io.izzel.taboolib.gradle.BukkitUtil
import io.izzel.taboolib.gradle.CommandHelper
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val taboolibVersion: String by project

plugins {
    java
    id("io.izzel.taboolib") version "2.0.27"
    id("com.github.johnrengelman.shadow") version "8.1.1" apply false
    kotlin("jvm") version "2.1.0"
}

subprojects {
    apply<JavaPlugin>()
    apply(plugin = "io.izzel.taboolib")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    // TabooLib 配置
    taboolib {
        env {
            install(Basic, Bukkit, BukkitUtil)
            install(CommandHelper)
        }
        version {
            taboolib = taboolibVersion
        }
    }

    // 全局仓库
    repositories {
        mavenCentral()
        maven("https://repo.hiusers.com/releases")
        maven("https://repo.codemc.io/repository/maven-releases/")
        maven("https://repo.codemc.io/repository/maven-snapshots/")
    }

    // 全局依赖
    dependencies {
        compileOnly("ink.ptms.core:v12104:12104:mapped")
        compileOnly("ink.ptms.core:v12104:12104:universal")

        compileOnly("com.google.code.gson:gson:2.8.7")

        implementation("top.maplex.arim:Arim:1.2.14")
        compileOnly("com.github.retrooper:packetevents-spigot:2.9.5")
    }
    // 编译配置
    java {
        withSourcesJar()
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = listOf("-Xjvm-default=all", "-Xextended-compiler-checks")
        }
    }

    tasks.test {
        useJUnitPlatform()
    }
}

gradle.buildFinished {
    buildDir.deleteRecursively()
}