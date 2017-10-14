package net.shadowfacts.krypton.ekt

import net.shadowfacts.krypton.Krypton
import net.shadowfacts.krypton.config.Configuration
import net.shadowfacts.krypton.pipeline.selector.PipelineSelectorExtension
import net.shadowfacts.krypton.util.dependencies.Dependencies
import java.io.File

/**
 * @author shadowfacts
 */
fun main(args: Array<String>) {
	val krypton = Krypton(Configuration {
		source = File("source")
		output = File("output")
	})

	krypton.createPipeline {
		selector = PipelineSelectorExtension("html")
		addStage(StageRenderEKT(null, File("source/_includes"), File("source/_layouts"), mapOf()), Dependencies {
		})
	}

	krypton.generate()
}