package com.alexeycode.kboy

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension

internal val Project.libs: VersionCatalog
    @Throws(IllegalStateException::class)
    get() {
        return extensions.findByType(VersionCatalogsExtension::class.java)?.named("libs")
            ?: error("Cannot find libraries in version catalog!")
    }