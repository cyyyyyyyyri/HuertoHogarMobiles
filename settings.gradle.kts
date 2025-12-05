pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Recomendado
    repositories {
        google()       // Repositorio de Google (Necesario para AndroidX, Maps, etc.)
        mavenCentral() // Repositorio central de Maven (Necesario para Moshi, Retrofit, OkHttp)
        // Añade cualquier otro repositorio que puedas necesitar (ej: jitpack)
    }
}

rootProject.name = "HuertoHogarMobiles"
include(":app")
 