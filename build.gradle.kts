plugins {
	id("net.fabricmc.fabric-loom")
}

version = providers.gradleProperty("mod_version").get()
group = providers.gradleProperty("maven_group").get()

loom {
//	splitEnvironmentSourceSets()

	mods {
		register("minedrift") {
			sourceSet(sourceSets.main.get())
//			sourceSet(sourceSets.getByName("client"))
		}
	}

	log4jConfigs.from.add(File(rootDir, "log4j-dev.xml"))
}

dependencies {
	minecraft("com.mojang:minecraft:${providers.gradleProperty("minecraft_version").get()}")
	implementation("net.fabricmc:fabric-loader:${providers.gradleProperty("loader_version").get()}")
	implementation("net.fabricmc.fabric-api:fabric-api:${providers.gradleProperty("fabric_api_version").get()}")

	implementation(project(":networking"))
}

tasks.processResources {
	val version = version
	inputs.property("version", version)

	filesMatching("fabric.mod.json") {
		expand("version" to version)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 25
}

java {
	// Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
	// if it is present.
	// If you remove this line, sources will not be generated.
	withSourcesJar()

	sourceCompatibility = JavaVersion.VERSION_25
	targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

	from("LICENSE") {
		rename { "${it}_$projectName" }
	}

	// Includes all subproject .class files, maybe there is better solution...
	project.subprojects.forEach { from(it.sourceSets.main.get().output) }
}

tasks.runClient {
	subprojects.forEach { it.tasks.named("runClient").get().enabled = false }
}
