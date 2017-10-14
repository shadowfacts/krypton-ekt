package net.shadowfacts.krypton.ekt

import net.shadowfacts.krypton.Krypton
import net.shadowfacts.krypton.config.Configuration
import net.shadowfacts.krypton.ekt.config.EKTConfig
import net.shadowfacts.krypton.ekt.config.ekt
import net.shadowfacts.krypton.ekt.pipeline.PipelineVoidIncludesLayouts
import net.shadowfacts.krypton.ekt.pipeline.stage.StageRenderEKT
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

		ekt = EKTConfig().apply {
			includesDir = File("source/_includes")
			layoutsDir = File("source/_layouts")
		}
	})

	krypton.createPipeline {
		selector = PipelineSelectorExtension("html")
		addStage(StageRenderEKT(mapOf()), Dependencies {
		})
	}
	krypton.addPipeline(PipelineVoidIncludesLayouts, 100)

	krypton.generate()
}